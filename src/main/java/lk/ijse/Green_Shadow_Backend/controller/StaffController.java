package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.RegisterStaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.EmailAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/staff")
@RequiredArgsConstructor
@Slf4j
public class StaffController {
    private final StaffService staffService;
    /**
     * Saves the staff member details.
     *
     * @param dto the staff member registration details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@Valid @RequestBody RegisterStaffDTO dto) {
        log.info("Attempting to save staff with email: {}", dto.getEmail());
        try {
            staffService.saveStaff(dto);
            log.info("Successfully saved staff with email: {}", dto.getEmail());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (EmailAlreadyExistException e) {
            log.warn("Email already exists for staff: {}", dto.getEmail());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist staff data for email: {}", dto.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates the staff member details.
     *
     * @param dto the updated staff member details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStaff(@Valid @RequestBody StaffDTO dto) {
        log.info("Attempting to update staff with ID: {}", dto.getId());
        try {
            staffService.updateStaff(dto);
            log.info("Successfully updated staff with ID: {}", dto.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found with ID: {}", dto.getId());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EmailAlreadyExistException e) {
            log.warn("Email already exists for staff with ID: {}", dto.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist updated staff data for ID: {}", dto.getId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes the staff member identified by the given ID.
     *
     * @param staffId the ID of the staff member to delete
     * @return ResponseEntity indicating the outcome of the operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(
            @Pattern(regexp = "S-\\d{3,}", message = "ID must start with 'S-' followed by at least three digits (e.g., S-001)")
            @PathVariable("id") String staffId) {
        log.info("Attempting to delete staff with ID: {}", staffId);
        try {
            staffService.deleteStaff(staffId);
            log.info("Successfully deleted staff with ID: {}", staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found with ID: {}", staffId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist deletion of staff with ID: {}", staffId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Retrieves the staff member details by the given ID.
     *
     * @param staffId the ID of the staff member to retrieve
     * @return ResponseEntity containing the staff details or a NOT_FOUND status if the staff member does not exist
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> findStaff(
            @Pattern(regexp = "S-\\d{3,}", message = "ID must start with 'S-' followed by at least three digits (e.g., S-001)")
            @PathVariable("id") String staffId) {
        log.info("Attempting to retrieve staff with ID: {}", staffId);
        try {
            StaffDTO staff = staffService.findStaffById(staffId);
            log.info("Successfully retrieved staff with ID: {}", staffId);
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found with ID: {}", staffId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a list of all staff members.
     *
     * @return ResponseEntity containing the list of all staff members
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StaffDTO>> findStaffs() {
        log.info("Attempting to retrieve all staff members");
        List<StaffDTO> staffList = staffService.findAllStaff();
        log.info("Successfully retrieved {} staff members", staffList.size());
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
}
