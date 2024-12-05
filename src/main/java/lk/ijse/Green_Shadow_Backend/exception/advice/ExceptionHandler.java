package lk.ijse.Green_Shadow_Backend.exception.advice;

import jakarta.mail.MessagingException;
import lk.ijse.Green_Shadow_Backend.customeObj.ResponseObj;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.validation.FieldError;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    /**
     * Handles validation errors for request body fields.
     *
     * @param ex MethodArgumentNotValidException object containing validation errors
     * @return ResponseEntity containing a ResponseObj with the first validation error
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObj> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().getFirst();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                (ResponseObj.builder().code(500).message(
                        "Validation failed on field '" + error.getField() + "': " + error.getDefaultMessage())
                        .build());
    }
    /**
     * Handles MessagingException and IOException that may occur during email-related operations.
     *
     * @param e Exception object (MessagingException or IOException)
     * @return ResponseEntity with an error message and HTTP status code
     */
    @org.springframework.web.bind.annotation.ExceptionHandler({MessagingException.class, IOException.class})
    public ResponseEntity<ResponseObj> handleEmailProcessingExceptions(Exception e) {
        log.error("Email processing error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body
                (ResponseObj.builder().code(500).message("Error occurred during email processing").build());
    }
    /**
     * This method handles BadCredentialsException, which occurs when invalid credentials are provided.
     *
     * @param e BadCredentialsException object
     * @return ResponseEntity with an error message and HTTP status code
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseObj> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Bad credentials: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body
                (ResponseObj.builder().code(401).message("Invalid username or password").build());
    }
    /**
     * Handles exceptions when the crop already exists.
     *
     * @param e CropAlreadyExistException object
     * @return ResponseEntity with error message and HTTP CONFLICT status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(CropAlreadyExistException.class)
    public ResponseEntity<ResponseObj> handleCropAlreadyExistException(CropAlreadyExistException e) {
        log.error("Crop already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body
                (ResponseObj.builder().code(409).message("Crop already exist").build());
    }
    /**
     * Handles exceptions when the crop is not found.
     *
     * @param e CropNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(CropNotFoundException.class)
    public ResponseEntity<ResponseObj> handleCropNotFoundException(CropNotFoundException e) {
        log.error("Crop not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Crop not found").build());
    }
    /**
     * Handles exceptions when data persistence fails.
     *
     * @param e DataPersistFailedException object
     * @return ResponseEntity with an error message and HTTP INTERNAL_SERVER_ERROR status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(DataPersistFailedException.class)
    public ResponseEntity<ResponseObj> handleDataPersistFailedException(DataPersistFailedException e) {
        log.error("Data persistence failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body
                (ResponseObj.builder().code(500).message("Data persist failed").build());
    }
    /**
     * Handles exceptions when the email already exists.
     *
     * @param e EmailAlreadyExistException object
     * @return ResponseEntity with error message and HTTP CONFLICT status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ResponseObj> handleEmailAlreadyExistException(EmailAlreadyExistException e) {
        log.error("Email already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body
                (ResponseObj.builder().code(409).message("Email already exist").build());
    }
    /**
     * Handles exceptions when equipment is not found.
     *
     * @param e EquipmentNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(EquipmentNotFoundException.class)
    public ResponseEntity<ResponseObj> handleEquipmentNotFoundException(EquipmentNotFoundException e) {
        log.error("Equipment not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Equipment not found").build());
    }
    /**
     * Handles exceptions when the field name already exists.
     *
     * @param e FieldNameAlreadyExistException object
     * @return ResponseEntity with error message and HTTP CONFLICT status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(FieldNameAlreadyExistException.class)
    public ResponseEntity<ResponseObj> handleFieldNameAlreadyExistException(FieldNameAlreadyExistException e) {
        log.error("Field name already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body
                (ResponseObj.builder().code(409).message("Field name already exist").build());
    }
    /**
     * Handles exceptions when the field is not found.
     *
     * @param e FieldNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(FieldNotFoundException.class)
    public ResponseEntity<ResponseObj> handleFieldNotFoundException(FieldNotFoundException e) {
        log.error("Field not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Field not found").build());
    }
    /**
     * Handles exceptions when an invalid OTP is provided.
     *
     * @param e InvalidOtpException object
     * @return ResponseEntity with an error message and HTTP BAD_REQUEST status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ResponseObj> handleInvalidOtpException(InvalidOtpException e) {
        log.error("Invalid OTP: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                (ResponseObj.builder().code(400).message("OTP invalid").build());
    }
    /**
     * Handles exceptions when the monitoring log is not found.
     *
     * @param e MonitoringLogNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MonitoringLogNotFoundException.class)
    public ResponseEntity<ResponseObj> handleMonitoringLogNotFoundException(MonitoringLogNotFoundException e) {
        log.error("Monitoring log not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Monitoring Log not found").build());
    }
    /**
     * Handles exceptions when staff is not found.
     *
     * @param e StaffNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(StaffNotFoundException.class)
    public ResponseEntity<ResponseObj> handleStaffNotFoundException(StaffNotFoundException e) {
        log.error("Staff not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Staff not found").build());
    }
    /**
     * Handles exceptions when a user already exists.
     *
     * @param e UserAlreadyExistException object
     * @return ResponseEntity with error message and HTTP CONFLICT status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ResponseObj> handleUserAlreadyExistException(UserAlreadyExistException e) {
        log.error("User already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body
                (ResponseObj.builder().code(409).message("User already exist").build());
    }
    /**
     * Handles exceptions when the user is not acceptable.
     *
     * @param e UserNotAcceptableException object
     * @return ResponseEntity with an error message and HTTP NOT_ACCEPTABLE status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotAcceptableException.class)
    public ResponseEntity<ResponseObj> handleUserNotAcceptableException(UserNotAcceptableException e) {
        log.error("User not acceptable: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body
                (ResponseObj.builder().code(406).message("User not acceptable").build());
    }
    /**
     * Handles exceptions when a user is not found.
     *
     * @param e UserNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseObj> handleUserNotFoundException(UserNotFoundException e) {
        log.error("User not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("User not found").build());
    }
    /**
     * Handles exceptions when the vehicle already exists.
     *
     * @param e VehicleAlreadyExistException object
     * @return ResponseEntity with error message and HTTP CONFLICT status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(VehicleAlreadyExistException.class)
    public ResponseEntity<ResponseObj> handleVehicleAlreadyExistException(VehicleAlreadyExistException e) {
        log.error("Vehicle already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body
                (ResponseObj.builder().code(409).message("Vehicle already exist").build());
    }
    /**
     * Handles exceptions when a vehicle is not found.
     *
     * @param e VehicleNotFoundException object
     * @return ResponseEntity with an error message and HTTP NOT_FOUND status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ResponseObj> handleVehicleNotFoundException(VehicleNotFoundException e) {
        log.error("Vehicle not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                (ResponseObj.builder().code(404).message("Vehicle not found").build());
    }
    /**
     * Handles all other exceptions not specifically handled by other methods.
     *
     * @param e Exception object
     * @return ResponseEntity with an error message and HTTP INTERNAL_SERVER_ERROR status
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObj> handleGeneralException(Exception e) {
        log.error("General error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body
                (ResponseObj.builder().code(500).message("An unexpected error occurred").build());
    }
}
