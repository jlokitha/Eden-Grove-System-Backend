package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.customeObj.ResponseObj;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.InvalidPasswordException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotAcceptableException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotFoundException;
import lk.ijse.Green_Shadow_Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {
    private final UserService userService;
    /**
     * Endpoint to update a user's details based on their email.
     * This method allows updating user information provided by the `UserCreateDTO`.
     *
     * @param email the email of the user to be updated
     * @param userCreateDTO the new details for the user to be updated
     * @param authorization the Authorization header containing the JWT token
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PutMapping(value = "/{email}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> updateUser(
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email should be valid")
            @PathVariable ("email") String email,
            @Valid @RequestBody UserCreateDTO userCreateDTO,
            @RequestHeader("Authorization") String authorization){
        try {
            log.info("Attempting to update user with email: {}", email);
            userCreateDTO.setEmail(email);
            userService.updateUser(userCreateDTO, authorization);
            log.info("Successfully updated user with email: {}", email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UserNotAcceptableException e) {
            log.warn("User with email {} update failed. Reason: Requested user not acceptable.", email);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(406).message("User not acceptable").build(),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (UserNotFoundException e) {
            log.warn("User with email {} not found for update.", email);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(404).message("User not found").build(),
                    HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            log.warn("User with email {} update failed. Reason: Invalid password.", email);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(400).message("Invalid password").build(),
                    HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Endpoint to delete a user based on their email.
     *
     * @param email the email of the user to be deleted
     * @return ResponseEntity indicating the outcome of the deletion operation
     */
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email should be valid")
            @PathVariable ("email") String email){
        try {
            log.info("Attempting to delete user with email: {}", email);
            userService.deleteUser(email);
            log.info("Successfully deleted user with email: {}", email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            log.warn("User with email {} not found for deletion.", email);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Endpoint to get all users, accessible only by users with the 'MANAGER' role.
     *
     * @return ResponseEntity containing a list of all users or an error status if failed
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size){
        log.info("Attempting to retrieve all users.");
        List<UserDTO> allUsers = userService.getAllUsers(page, size);
        log.info("Successfully retrieved {} users.", allUsers.size());
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    /**
     * Retrieves a list of users based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of users
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> filterUser(@RequestBody UserFilterDTO filterDTO) {
        log.info("Attempting to filter user with criteria: {}", filterDTO);
        try {
            List<UserDTO> userDTOS = userService.filterUser(filterDTO);
            log.info("Successfully filtered user. Found {} results.", userDTOS.size());
            return new ResponseEntity<>(userDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to filter user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
