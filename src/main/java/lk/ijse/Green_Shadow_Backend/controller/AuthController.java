package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.jwtmodels.JwtAuthResponse;
import lk.ijse.Green_Shadow_Backend.jwtmodels.SignIn;
import lk.ijse.Green_Shadow_Backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin("*")
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
        log.info("Attempting to sign up user with email: {}", userCreateDTO.getEmail());
        JwtAuthResponse response = authenticationService.signUp(userCreateDTO);
        log.info("Successfully signed up user with email: {}", userCreateDTO.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
        log.info("Attempting to sign in user with email: {}", signIn.getEmail());
        JwtAuthResponse jwtAuthResponse = authenticationService.signIn(signIn);
        log.info("Successfully signed in user with email: {}", signIn.getEmail());
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
    /**
     * Handles the password reset request by verifying the OTP and resetting the user's password.
     *
     * @param userCreateDTO contains the email and new password for resetting the user's password.
     * @return ResponseEntity with status OK if successful, or an error status if failed.
     */
    @PostMapping(
            value = "/reset_password",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("Attempting to reset password for email: {}", userCreateDTO.getEmail());
        authenticationService.resetUserPassword(userCreateDTO);
        log.info("Successfully reset password for email: {}", userCreateDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * Sends an OTP to the user's email for verification.
     *
     * @param email the email of the user requesting OTP.
     * @return ResponseEntity with status OK if successful, or an error status if failed.
     */
    @GetMapping(
            value = "/request_otp",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> requestOtp(@RequestParam("email") String email) {
        try {
            log.info("Attempting to send OTP for email: {}", email);
            authenticationService.verifyUserEmail(email);
            log.info("Successfully sent OTP for email: {}", email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MessagingException | IOException e) {
            log.error("Error during email verification for email: {}. Error: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        log.info("Attempting to refresh token with provided token.");
        JwtAuthResponse jwtAuthResponse = authenticationService.refreshToken(token);
        log.info("Successfully refreshed token.");
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}
