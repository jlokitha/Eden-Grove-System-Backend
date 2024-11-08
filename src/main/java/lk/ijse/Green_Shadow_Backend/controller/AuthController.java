package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.UserAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotFoundException;
import lk.ijse.Green_Shadow_Backend.jwtmodels.JwtAuthResponse;
import lk.ijse.Green_Shadow_Backend.jwtmodels.SignIn;
import lk.ijse.Green_Shadow_Backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {
    private final AuthenticationService authenticationService;
    /**
     * Handles user sign-up by creating a new user and returning a JWT authentication response.
     *
     * @param userCreateDTO contains the user details for registration.
     * @return ResponseEntity with a JWT response if successful, or an error status if failed.
     */
    @PostMapping(
            value = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            log.info("Attempting to sign up user with email: {}", userCreateDTO.getEmail());
            JwtAuthResponse response = authenticationService.signUp(userCreateDTO);
            log.info("Successfully signed up user with email: {}", userCreateDTO.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            log.warn("User already exists with email: {}", userCreateDTO.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (StaffNotFoundException e) {
            log.error("Staff not found while registering user with email: {}", userCreateDTO.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Data persistence failed while registering user with email: {}", userCreateDTO.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Handles user sign-in by validating the credentials and returning a JWT token.
     *
     * @param signIn contains the user's email and password for authentication.
     * @return ResponseEntity with the JWT token if successful, or an error status if failed.
     */
    @PostMapping(
            value = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody SignIn signIn) {
        try {
            log.info("Attempting to sign in user with email: {}", signIn.getEmail());
            JwtAuthResponse jwtAuthResponse = authenticationService.signIn(signIn);
            log.info("Successfully signed in user with email: {}", signIn.getEmail());
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.warn("User not found with email: {}", signIn.getEmail());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials provided for user with email: {}", signIn.getEmail());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Refreshes the JWT token using a valid refresh token.
     *
     * @param token the refresh token to be used to generate a new JWT token.
     * @return ResponseEntity with the refreshed JWT token, or an error status if failed.
     */
    @PostMapping(
            value = "/refresh",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> refreshToken (@RequestParam ("token") String token) {
        try {
            log.info("Attempting to refresh token with provided token.");
            JwtAuthResponse jwtAuthResponse = authenticationService.refreshToken(token);
            log.info("Successfully refreshed token.");
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.warn("User not found for refresh token request.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
