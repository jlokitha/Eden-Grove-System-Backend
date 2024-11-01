package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldAssociateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDTO;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.service.FieldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/field")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FieldController {
    private final FieldService fieldService;
    /**
     * Saves a new field with the provided details.
     *
     * @param fieldDTO the data transfer object containing field details
     * @return ResponseEntity indicating the outcome of the operation
     */
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
     * Updates the association of a field with the provided details.
     *
     * @param fieldId the ID of the field to update, must match the pattern "F-XXX"
     * @param fieldAssociateDTO the data transfer object containing updated association details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFieldAssociate(
            @Pattern(regexp = "F-\\d{3,}", message = "ID must start with 'F-' followed by at least three digits (e.g., F-001)")
            @PathVariable("id") String fieldId,
            @Valid @RequestBody FieldAssociateDTO fieldAssociateDTO) {
        try {
            log.info("Attempting to update field association with ID: {}", fieldId);
            fieldAssociateDTO.setFCode(fieldId);
            fieldService.updateFieldAssociate(fieldAssociateDTO);
            log.info("Successfully updated field association with ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException | CropNotFoundException | StaffNotFoundException e) {
            log.warn("Field association update failed for ID: {} | Field, Crop or Staff not found.", fieldId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist updated field association data for ID: {}", fieldId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes a field with the specified ID.
     *
     * @param fieldId the ID of the field to delete, must match the pattern "F-XXX"
     * @return ResponseEntity indicating the outcome of the operation
     */
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
        } catch (FieldDeletionException e) {
            log.warn("Conflict occurred while trying to delete field with ID: {}", fieldId, e);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist changes after deleting field with ID: {}", fieldId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<List<FieldDTO>> findAllFields() {
        log.info("Retrieving all fields");
        List<FieldDTO> fieldDTOs = fieldService.findAllFields();
        log.info("Successfully retrieved {} fields", fieldDTOs.size());
        return new ResponseEntity<>(fieldDTOs, HttpStatus.OK);
    }
}
