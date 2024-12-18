package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.customeObj.ResponseObj;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.service.EquipmentService;
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
@RequestMapping("api/v1/equipment")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class EquipmentController {
    private final EquipmentService equipmentService;
    /**
     * Saves new equipment with the provided details.
     *
     * @param equipmentDTO the data transfer object containing equipment details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> saveEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) {
        log.info("Attempting to save a new equipment with name: {}", equipmentDTO.getName());
        equipmentService.saveEquipment(equipmentDTO);
        log.info("Successfully saved equipment with name: {}", equipmentDTO.getName());
        return new ResponseEntity<>(
                ResponseObj.builder().code(201).message("Successfully saved vehicle").build(),
                HttpStatus.CREATED);
    }
    /**
     * Updates existing equipment with the provided details.
     *
     * @param equipmentId the ID of the equipment to update
     * @param equipmentDTO the data transfer object containing updated equipment details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEquipment(
            @Pattern(regexp = "E-\\d{3,}", message = "ID must start with 'E-' followed by at least three digits (e.g., E-001)")
            @PathVariable("id") String equipmentId,
            @Valid @RequestBody EquipmentDTO equipmentDTO) {
        log.info("Attempting to update equipment with ID: {}", equipmentId);
        equipmentDTO.setEquipmentId(equipmentId);
        equipmentService.updateEquipment(equipmentDTO);
        log.info("Successfully updated equipment with ID: {}", equipmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * Deletes existing equipment by its ID.
     *
     * @param equipmentId the ID of the equipment to delete
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseObj> deleteEquipment(
            @Pattern(regexp = "E-\\d{3,}", message = "ID must start with 'E-' followed by at least three digits (e.g., E-001)")
            @PathVariable("id") String equipmentId) {
        log.info("Attempting to delete equipment with ID: {}", equipmentId);
        equipmentService.deleteEquipment(equipmentId);
        log.info("Successfully deleted equipment with ID: {}", equipmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        log.info("Attempting to find equipment with ID: {}", equipmentId);
        EquipmentDTO equipmentDTO = equipmentService.findEquipmentById(equipmentId);
        log.info("Successfully found equipment with ID: {}", equipmentId);
        return new ResponseEntity<>(equipmentDTO, HttpStatus.OK);
    }
    /**
     * Retrieves a list of all equipment.
     *
     * @return ResponseEntity containing the list of EquipmentDTOs and HTTP status
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipmentDTO>> findAllEquipment(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Attempting to retrieve all equipments");
        List<EquipmentDTO> equipmentDTOS;
        if (page == null || size == null) {
            log.info("No pagination parameters provided, retrieving all equipments");
            equipmentDTOS = equipmentService.findAllEquipments();
        } else {
            log.info("Retrieving equipments with pagination - page: {}, size: {}", page, size);
            equipmentDTOS = equipmentService.findAllEquipments(page, size);
        }
        log.info("Successfully retrieved {} equipments", equipmentDTOS.size());
        return new ResponseEntity<>(equipmentDTOS, HttpStatus.OK);
    }
    /**
     * Retrieves a list of equipment based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of equipments
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipmentDTO>> filterEquipment(@RequestBody EquipmentFilterDTO filterDTO) {
        log.info("Attempting to filter equipments with criteria: {}", filterDTO);
        List<EquipmentDTO> equipmentDTOS = equipmentService.filterAllEquipments(filterDTO);
        log.info("Successfully filtered equipments. Found {} results.", equipmentDTOS.size());
        return new ResponseEntity<>(equipmentDTOS, HttpStatus.OK);
    }
}
