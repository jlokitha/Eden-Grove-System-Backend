package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.customeObj.ResponseObj;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleFilterDTO;
import lk.ijse.Green_Shadow_Backend.service.VehicleService;
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
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class VehicleController {
    private final VehicleService vehicleService;
    /**
     * Saves a new vehicle.
     *
     * @param vehicleDTO the VehicleDTO object containing vehicle details
     * @return ResponseEntity indicating the result of the save operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObj> saveVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        log.info("Attempting to save a new vehicle with license plate number: {}", vehicleDTO.getLicensePlateNo());
        vehicleService.saveVehicle(vehicleDTO);
        log.info("Successfully saved vehicle with license plate number: {}", vehicleDTO.getLicensePlateNo());
        return new ResponseEntity<>(
                ResponseObj.builder().code(201).message("Successfully saved vehicle").build(),
                HttpStatus.CREATED);
    }
    /**
     * Updates an existing vehicle by ID.
     *
     * @param vehicleCode the ID of the vehicle to be updated
     * @param vehicleDTO the VehicleDTO object containing updated vehicle details
     * @return ResponseEntity indicating the result of the update operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateVehicle(
            @Pattern(regexp = "V-\\d{3,}", message = "ID must start with 'V-' followed by at least three digits (e.g., V-001)")
            @PathVariable("id") String vehicleCode,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        log.info("Attempting to update vehicle with ID: {}", vehicleCode);
        vehicleDTO.setVehicleCode(vehicleCode);
        vehicleService.updateVehicle(vehicleDTO);
        log.info("Successfully updated vehicle with ID: {}", vehicleCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * Deletes a vehicle by ID.
     *
     * @param vehicleCode the ID of the vehicle to be deleted
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMINISTRATIVE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @Pattern(regexp = "V-\\d{3,}", message = "ID must start with 'V-' followed by at least three digits (e.g., V-001)")
            @PathVariable("id") String vehicleCode) {
        log.info("Attempting to delete vehicle with ID: {}", vehicleCode);
        vehicleService.deleteVehicle(vehicleCode);
        log.info("Successfully deleted vehicle with ID: {}", vehicleCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        log.info("Attempting to find vehicle with ID: {}", vehicleCode);
        VehicleDTO vehicle = vehicleService.findVehicleById(vehicleCode);
        log.info("Successfully found vehicle with ID: {}", vehicleCode);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }
    /**
     * Retrieves all vehicles from the database.
     *
     * @return ResponseEntity containing a list of VehicleDTOs and HTTP status
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDTO>> findAllVehicles(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size) {
        log.info("Attempting to retrieve all vehicles");
        List<VehicleDTO> vehicles = vehicleService.findAllVehicles(page, size);
        log.info("Successfully retrieved all vehicles");
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
    /**
     * Retrieves a list of vehicle based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of vehicles
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDTO>> filterVehicle(@RequestBody VehicleFilterDTO filterDTO) {
        log.info("Attempting to filter vehicles with criteria: {}", filterDTO);
        List<VehicleDTO> vehicleDTOS = vehicleService.filterVehicle(filterDTO);
        log.info("Successfully filtered vehicles. Found {} results.", vehicleDTOS.size());
        return new ResponseEntity<>(vehicleDTOS, HttpStatus.OK);
    }
}
