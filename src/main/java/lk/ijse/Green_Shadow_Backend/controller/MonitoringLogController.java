package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.service.MonitoringLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monitoringlog")
@RequiredArgsConstructor
@Slf4j
@Validated
public class MonitoringLogController {
    private final MonitoringLogService monitoringLogService;
    /**
     * Saves a new monitoring log with the provided details.
     *
     * @param monitoringLogDTO the data transfer object containing monitoring log details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveMonitoringLog(@Valid @ModelAttribute MonitoringLogCreateDTO monitoringLogDTO) {
        try {
            log.info("Attempting to save a new monitoring log for field: {}", monitoringLogDTO.getFieldCode());
            monitoringLogService.saveMonitoringLog(monitoringLogDTO);
            log.info("Successfully saved monitoring log for field: {}", monitoringLogDTO.getFieldCode());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (StaffNotFoundException | CropNotFoundException | FieldNotFoundException e) {
            log.warn("Related entity not found for monitoring log: {}", e.getClass().getSimpleName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to save monitoring log for field: {}", monitoringLogDTO.getFieldCode(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates an existing monitoring log with the provided details.
     *
     * @param id the ID of the monitoring log to update, must start with 'M-' followed by at least three digits
     * @param monitoringLogDTO the data transfer object containing updated monitoring log details
     * @return ResponseEntity indicating the outcome of the update operation
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMonitoringLog(
            @Pattern(regexp = "M-\\d{3,}", message = "ID must start with 'M-' followed by at least three digits (e.g., M-001)")
            @PathVariable("id") String id,
            @Valid @ModelAttribute MonitoringLogCreateDTO monitoringLogDTO) {
        try {
            log.info("Attempting to update monitoring log with ID: {}", id);
            monitoringLogDTO.setLogCode(id);
            monitoringLogService.updateMonitoringLog(monitoringLogDTO);
            log.info("Successfully updated monitoring log with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MonitoringLogNotFoundException | StaffNotFoundException | CropNotFoundException | FieldNotFoundException e) {
            log.warn("Entity not found during update for monitoring log ID: {} - Exception: {}", id, e.getClass().getSimpleName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to update monitoring log with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes a monitoring log by its ID.
     *
     * @param id the ID of the monitoring log to delete, must start with 'M-' followed by at least three digits
     * @return ResponseEntity indicating the outcome of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringLog(
            @Pattern(regexp = "M-\\d{3,}", message = "ID must start with 'M-' followed by at least three digits (e.g., M-001)")
            @PathVariable("id") String id) {
        try {
            log.info("Attempting to delete monitoring log with ID: {}", id);
            monitoringLogService.deleteMonitoringLog(id);
            log.info("Successfully deleted monitoring log with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MonitoringLogNotFoundException e) {
            log.warn("Monitoring log not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to delete monitoring log with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Retrieves a monitoring log by its ID.
     *
     * @param id the ID of the monitoring log to retrieve, must start with 'M-' followed by at least three digits
     * @return ResponseEntity containing the monitoring log details or a status indicating the outcome of the retrieval
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMonitoringLog(
            @Pattern(regexp = "M-\\d{3,}", message = "ID must start with 'M-' followed by at least three digits (e.g., M-001)")
            @PathVariable("id") String id) {
        try {
            log.info("Attempting to retrieve monitoring log with ID: {}", id);
            MonitoringLogDTO logDTO = monitoringLogService.findMonitoringLogById(id);
            log.info("Successfully retrieved monitoring log with ID: {}", id);
            return new ResponseEntity<>(logDTO, HttpStatus.OK);
        } catch (MonitoringLogNotFoundException e) {
            log.warn("Monitoring log not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllMonitoringLogs() {
        log.info("Attempting to retrieve all monitoring logs");
        List<MonitoringLogDTO> monitoringLogs = monitoringLogService.findAllMonitoringLogs();
        log.info("Successfully retrieved all monitoring logs");
        return new ResponseEntity<>(monitoringLogs, HttpStatus.OK);
    }
}
