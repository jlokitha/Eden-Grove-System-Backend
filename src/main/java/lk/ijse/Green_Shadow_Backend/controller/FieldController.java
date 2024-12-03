package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.service.FieldService;
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
@RequestMapping("api/v1/field")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class FieldController {
    private final FieldService fieldService;
    /**
     * Saves a new field with the provided details.
     *
     * @param fieldDTO the data transfer object containing field details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveField(@Valid @ModelAttribute FieldCreateDTO fieldDTO) {
        try {
            log.info("Attempting to save a new field with name: {}", fieldDTO.getFieldName());
            fieldService.saveField(fieldDTO);
            log.info("Successfully saved field with name: {}", fieldDTO.getFieldName());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FieldNameAlreadyExistException e) {
            log.warn("Field name already exists: {}", fieldDTO.getFieldName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist field data for field name: {}", fieldDTO.getFieldName(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates an existing field with the provided details.
     *
     * @param fieldId the ID of the field to update (must start with 'F-' followed by at least three digits)
     * @param fieldDTO the data transfer object containing updated field details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateField(
            @Pattern(regexp = "F-\\d{3,}", message = "ID must start with 'F-' followed by at least three digits (e.g., F-001)")
            @PathVariable("id") String fieldId,
            @Valid @ModelAttribute FieldCreateDTO fieldDTO) {
        try {
            log.info("Attempting to update field with ID: {}", fieldId);
            fieldDTO.setFCode(fieldId);
            fieldService.updateField(fieldDTO);
            log.info("Successfully updated field with ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException e) {
            log.warn("Field,  not found with ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist updated field data for ID: {}", fieldId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes a field with the specified ID.
     *
     * @param fieldId the ID of the field to delete, must match the pattern "F-XXX"
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(
            @Pattern(regexp = "F-\\d{3,}", message = "ID must start with 'F-' followed by at least three digits (e.g., F-001)")
            @PathVariable("id") String fieldId) {
        try {
            log.info("Attempting to delete field with ID: {}", fieldId);
            fieldService.deleteField(fieldId);
            log.info("Successfully deleted field with ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException e) {
            log.warn("Field not found for deletion with ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a field by its ID.
     *
     * @param fieldId the ID of the field to retrieve, must match the pattern "F-XXX"
     * @return ResponseEntity containing the FieldDTO if found, or NOT_FOUND status if not
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FieldDTO> findFieldById(
            @Pattern(regexp = "F-\\d{3,}", message = "ID must start with 'F-' followed by at least three digits (e.g., F-001)")
            @PathVariable("id") String fieldId) {
        try {
            log.info("Attempting to find field with ID: {}", fieldId);
            FieldDTO fieldDTO = fieldService.findFieldById(fieldId);
            log.info("Field found with ID: {}", fieldId);
            return new ResponseEntity<>(fieldDTO, HttpStatus.OK);
        } catch (FieldNotFoundException e) {
            log.warn("Field not found for ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a list of all fields.
     *
     * @return ResponseEntity containing a list of FieldDTOs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDTO>> findAllFields(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Attempting to retrieve all fields");
        List<FieldDTO> fieldDTOS;
        if (page == null || size == null) {
            log.info("No pagination parameters provided, retrieving all fields");
            fieldDTOS = fieldService.findAllFields();
        } else {
            log.info("Retrieving fields with pagination - page: {}, size: {}", page, size);
            fieldDTOS = fieldService.findAllFields(page, size);
        }
        log.info("Successfully retrieved {} fields", fieldDTOS.size());
        return new ResponseEntity<>(fieldDTOS, HttpStatus.OK);
    }
    /**
     * Retrieves a list of field based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of field
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDTO>> filterField(@RequestBody FieldFilterDTO filterDTO) {
        log.info("Attempting to filter field with criteria: {}", filterDTO);
        try {
            List<FieldDTO> fieldDTOS = fieldService.filterFields(filterDTO);
            log.info("Successfully filtered fields. Found {} results.", fieldDTOS.size());
            return new ResponseEntity<>(fieldDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to filter field", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
