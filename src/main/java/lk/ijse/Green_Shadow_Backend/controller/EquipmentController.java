package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.EquipmentDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.EquipmentNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.FieldNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/equipment")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EquipmentController {
    private final EquipmentService equipmentService;
    /**
     * Saves new equipment with the provided details.
     *
     * @param equipmentDTO the data transfer object containing equipment details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) {
        try {
            log.info("Attempting to save a new equipment with name: {}", equipmentDTO.getName());
            equipmentService.saveEquipment(equipmentDTO);
            log.info("Successfully saved equipment with name: {}", equipmentDTO.getName());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (FieldNotFoundException e) {
            log.warn("Field not available for equipment: {}", equipmentDTO.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not available for equipment: {}", equipmentDTO.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to save equipment with name: {}", equipmentDTO.getName(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates existing equipment with the provided details.
     *
     * @param equipmentId the ID of the equipment to update
     * @param equipmentDTO the data transfer object containing updated equipment details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEquipment(
            @Pattern(regexp = "E-\\d{3,}", message = "ID must start with 'E-' followed by at least three digits (e.g., E-001)")
            @PathVariable("id") String equipmentId,
            @Valid @RequestBody EquipmentDTO equipmentDTO) {
        try {
            log.info("Attempting to update equipment with ID: {}", equipmentId);
            equipmentDTO.setEquipmentId(equipmentId);
            equipmentService.updateEquipment(equipmentDTO);
            log.info("Successfully updated equipment with ID: {}", equipmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EquipmentNotFoundException e) {
            log.warn("Equipment not found with ID: {}", equipmentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (FieldNotFoundException e) {
            log.warn("Field not available for equipment: {}", equipmentDTO.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (StaffNotFoundException e) {
            log.warn("Staff not available for equipment: {}", equipmentDTO.getName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to update equipment with ID: {}", equipmentId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes existing equipment by its ID.
     *
     * @param equipmentId the ID of the equipment to delete
     * @return ResponseEntity indicating the outcome of the operation
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteEquipment(
            @Pattern(regexp = "E-\\d{3,}", message = "ID must start with 'E-' followed by at least three digits (e.g., E-001)")
            @PathVariable("id") String equipmentId) {
        try {
            log.info("Attempting to delete equipment with ID: {}", equipmentId);
            equipmentService.deleteEquipment(equipmentId);
            log.info("Successfully deleted equipment with ID: {}", equipmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EquipmentNotFoundException e) {
            log.warn("Equipment not found with ID: {}", equipmentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to delete equipment with ID: {}", equipmentId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Retrieves existing equipment by its ID.
     *
     * @param equipmentId the ID of the equipment to retrieve
     * @return ResponseEntity containing the EquipmentDTO and HTTP status
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EquipmentDTO> findEquipment(
            @Pattern(regexp = "E-\\d{3,}", message = "ID must start with 'E-' followed by at least three digits (e.g., E-001)")
            @PathVariable("id") String equipmentId) {
        try {
            log.info("Attempting to find equipment with ID: {}", equipmentId);
            EquipmentDTO equipmentDTO = equipmentService.findEquipmentById(equipmentId);
            log.info("Successfully found equipment with ID: {}", equipmentId);
            return new ResponseEntity<>(equipmentDTO, HttpStatus.OK);
        } catch (EquipmentNotFoundException e) {
            log.warn("Equipment not found with ID: {}", equipmentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a list of all equipment.
     *
     * @return ResponseEntity containing the list of EquipmentDTOs and HTTP status
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipmentDTO>> findAllEquipment() {
        log.info("Attempting to find all equipment");
        List<EquipmentDTO> equipmentDTOs = equipmentService.findAllEquipments();
        log.info("Successfully found all equipment");
        return new ResponseEntity<>(equipmentDTOs, HttpStatus.OK);
    }
}
