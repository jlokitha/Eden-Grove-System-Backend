package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.VehicleAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.VehicleNotFoundException;
import lk.ijse.Green_Shadow_Backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
@Slf4j
@Validated
public class VehicleController {
    private final VehicleService vehicleService;
    /**
     * Saves a new vehicle.
     *
     * @param vehicleDTO the VehicleDTO object containing vehicle details
     * @return ResponseEntity indicating the result of the save operation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            log.info("Attempting to save a new vehicle with license plate number: {}", vehicleDTO.getLicensePlateNo());
            vehicleService.saveVehicle(vehicleDTO);
            log.info("Successfully saved vehicle with license plate number: {}", vehicleDTO.getLicensePlateNo());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (VehicleAlreadyExistException e) {
            log.warn("Vehicle with license plate number already exists: {}", vehicleDTO.getLicensePlateNo());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (StaffNotFoundException e) {
            log.error("Active staff not found for staffId: {}", vehicleDTO.getStaff(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist vehicle data for vehicle with license plate number: {}", vehicleDTO.getLicensePlateNo(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates an existing vehicle by ID.
     *
     * @param vehicleCode the ID of the vehicle to be updated
     * @param vehicleDTO the VehicleDTO object containing updated vehicle details
     * @return ResponseEntity indicating the result of the update operation
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateVehicle(
            @Pattern(regexp = "V-\\d{3,}", message = "ID must start with 'V-' followed by at least three digits (e.g., V-001)")
            @PathVariable("id") String vehicleCode,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        try {
            log.info("Attempting to update vehicle with ID: {}", vehicleCode);
            vehicleDTO.setVehicleCode(vehicleCode);
            vehicleService.updateVehicle(vehicleDTO);
            log.info("Successfully updated vehicle with ID: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (VehicleNotFoundException e) {
            log.warn("Vehicle not found for ID: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (StaffNotFoundException e) {
            log.error("Active staff not found for staffId: {}", vehicleDTO.getStaff(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to persist updated vehicle data for vehicle with ID: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes a vehicle by ID.
     *
     * @param vehicleCode the ID of the vehicle to be deleted
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @Pattern(regexp = "V-\\d{3,}", message = "ID must start with 'V-' followed by at least three digits (e.g., V-001)")
            @PathVariable("id") String vehicleCode) {
        try {
            log.info("Attempting to delete vehicle with ID: {}", vehicleCode);
            vehicleService.deleteVehicle(vehicleCode);
            log.info("Successfully deleted vehicle with ID: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (VehicleNotFoundException e) {
            log.warn("Vehicle not found for ID: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to delete vehicle with ID: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Retrieves a vehicle by ID.
     *
     * @param vehicleCode the ID of the vehicle to be retrieved
     * @return ResponseEntity containing the VehicleDTO and HTTP status
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDTO> findVehicle(
            @Pattern(regexp = "V-\\d{3,}", message = "ID must start with 'V-' followed by at least three digits (e.g., V-001)")
            @PathVariable("id") String vehicleCode) {
        try {
            log.info("Attempting to find vehicle with ID: {}", vehicleCode);
            VehicleDTO vehicle = vehicleService.findVehicleById(vehicleCode);
            log.info("Successfully found vehicle with ID: {}", vehicleCode);
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } catch (VehicleNotFoundException e) {
            log.warn("Vehicle not found for ID: {}", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves all vehicles from the database.
     *
     * @return ResponseEntity containing a list of VehicleDTOs and HTTP status
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDTO>> findAllVehicles() {
        log.info("Attempting to retrieve all vehicles");
        List<VehicleDTO> vehicles = vehicleService.findAllVehicles();
        log.info("Successfully retrieved all vehicles");
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
}
