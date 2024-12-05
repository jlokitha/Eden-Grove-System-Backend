package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.service.MonitoringLogService;
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
@RequestMapping("/api/v1/monitoringlog")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MonitoringLogController {
    private final MonitoringLogService monitoringLogService;
    /**
     * Saves a new monitoring log with the provided details.
     *
     * @param monitoringLogDTO the data transfer object containing monitoring log details
     * @return ResponseEntity indicating the outcome of the operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveMonitoringLog(@Valid @ModelAttribute MonitoringLogCreateDTO monitoringLogDTO) {
        log.info("Attempting to save a new monitoring log for field: {}", monitoringLogDTO.getFieldCode());
        monitoringLogService.saveMonitoringLog(monitoringLogDTO);
        log.info("Successfully saved monitoring log for field: {}", monitoringLogDTO.getFieldCode());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * Updates an existing monitoring log with the provided details.
     *
     * @param id the ID of the monitoring log to update, must start with 'L-' followed by at least three digits
     * @param monitoringLogDTO the data transfer object containing updated monitoring log details
     * @return ResponseEntity indicating the outcome of the update operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMonitoringLog(
            @Pattern(regexp = "L-\\d{3,}", message = "ID must start with 'L-' followed by at least three digits (e.g., L-001)")
            @PathVariable("id") String id,
            @Valid @ModelAttribute MonitoringLogCreateDTO monitoringLogDTO) {
        log.info("Attempting to update monitoring log with ID: {}", id);
        monitoringLogDTO.setLogCode(id);
        monitoringLogService.updateMonitoringLog(monitoringLogDTO);
        log.info("Successfully updated monitoring log with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * Retrieves a monitoring log by its ID.
     *
     * @param id the ID of the monitoring log to retrieve, must start with 'L-' followed by at least three digits
     * @return ResponseEntity containing the monitoring log details or a status indicating the outcome of the retrieval
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMonitoringLog(
            @Pattern(regexp = "L-\\d{3,}", message = "ID must start with 'L-' followed by at least three digits (e.g., L-001)")
            @PathVariable("id") String id) {
        log.info("Attempting to retrieve monitoring log with ID: {}", id);
        MonitoringLogDTO logDTO = monitoringLogService.findMonitoringLogById(id);
        log.info("Successfully retrieved monitoring log with ID: {}", id);
        return new ResponseEntity<>(logDTO, HttpStatus.OK);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllMonitoringLogs(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Attempting to retrieve all logs");
        List<MonitoringLogDTO> fieldDTOS;
        if (page == null || size == null) {
            log.info("No pagination parameters provided, retrieving all logs");
            fieldDTOS = monitoringLogService.findAllMonitoringLogs();
        } else {
            log.info("Retrieving logs with pagination - page: {}, size: {}", page, size);
            fieldDTOS = monitoringLogService.findAllMonitoringLogs(page, size);
        }
        log.info("Successfully retrieved {} logs", fieldDTOS.size());
        return new ResponseEntity<>(fieldDTOS, HttpStatus.OK);
    }
    /**
     * Retrieves a list of logs based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of logs
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonitoringLogDTO>> filterField(@RequestBody MonitoringLogFilterDTO filterDTO) {
        log.info("Attempting to filter field with criteria: {}", filterDTO);
        List<MonitoringLogDTO> logDtoS = monitoringLogService.filterMonitoringLogs(filterDTO);
        log.info("Successfully filtered fields. Found {} results.", logDtoS.size());
        return new ResponseEntity<>(logDtoS, HttpStatus.OK);
    }
}
