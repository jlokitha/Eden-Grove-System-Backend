package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.RegisterStaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffFilterDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.EmailAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.service.StaffService;
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
@RequestMapping("api/v1/staff")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StaffController {
    private final StaffService staffService;
    /**
     * Saves the staff member details.
     *
     * @param dto the staff member registration details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@Valid @RequestBody RegisterStaffDTO dto) {
        System.out.println("dto = " + dto);
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
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
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
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
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
     * Retrieves the staff member details using the provided JWT token.
     *
     * @param token the JWT token used to identify the staff member
     * @return ResponseEntity containing the staff details or a NOT_FOUND status if the staff member does not exist
     */
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffDTO> findStaffByToken(@RequestHeader("Authorization") String token) {
        log.info("Attempting to retrieve staff with token");
        try {
            StaffDTO staff = staffService.findStaffByToken(token);
            log.info("Successfully retrieved staff with token");
            return new ResponseEntity<>(staff, HttpStatus.OK);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not found with token");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a list of all staff members.
     *
     * @return ResponseEntity containing the list of all staff members
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StaffDTO>> findStaffs(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Attempting to retrieve all staff members");
        List<StaffDTO> staffList;
        if (page == null || size == null) {
            log.info("No pagination parameters provided, retrieving all staff members");
            staffList = staffService.findAllStaff();
        } else {
            log.info("Retrieving staff members with pagination - page: {}, size: {}", page, size);
            staffList = staffService.findAllStaff(page, size);
        }
        log.info("Successfully retrieved {} staff members", staffList.size());
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
    /**
     * Retrieves a list of staff members based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of staff members
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StaffDTO>> filterStaff(@RequestBody StaffFilterDTO filterDTO) {
        log.info("Attempting to filter staff with criteria: {}", filterDTO);
        try {
            List<StaffDTO> filteredStaff = staffService.filterStaff(filterDTO);
            log.info("Successfully filtered staff members. Found {} results.", filteredStaff.size());
            return new ResponseEntity<>(filteredStaff, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to filter staff", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
