package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
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
    public ResponseEntity<Void> updateUser(
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
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (UserNotFoundException e) {
            log.warn("User with email {} not found for update.", email);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Error occurred while updating user with email: {}", email, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Endpoint to delete a user based on their email.
     *
     * @param email the email of the user to be deleted
     * @return ResponseEntity indicating the outcome of the deletion operation
     */
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
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist changes while deleting user with email: {}", email, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Endpoint to get all users, accessible only by users with the 'MANAGER' role.
     *
     * @return ResponseEntity containing a list of all users or an error status if failed
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        log.info("Attempting to retrieve all users.");
        List<UserDTO> allUsers = userService.getAllUsers();
        log.info("Successfully retrieved {} users.", allUsers.size());
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
