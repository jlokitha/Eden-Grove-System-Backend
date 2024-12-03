package lk.ijse.Green_Shadow_Backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.customeObj.ResponseObj;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.service.CropService;
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
@RequestMapping("api/v1/crop")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CropController {
    private final CropService cropService;
    /**
     * Saves a new crop.
     *
     * @param cropDTO the crop data to be saved
     * @return ResponseEntity indicating the result of the save operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> saveCrop(@Valid @ModelAttribute CropCreateDTO cropDTO) {
        try {
            log.info("Attempting to save crop: {}", cropDTO);
            cropService.saveCrop(cropDTO);
            log.info("Crop saved successfully: {}", cropDTO);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(201).message("Crop saved successfully")
                            .build(), HttpStatus.CREATED);
        } catch (CropAlreadyExistException e) {
            log.warn("Crop already exists: {}", cropDTO);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(400).message("Crop already exists")
                    .build(), HttpStatus.BAD_REQUEST);
        } catch (DataPersistFailedException e) {
            log.error("Failed to save crop: {}", cropDTO, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Updates an existing crop.
     *
     * @param cropCode the ID of the crop to be updated (must start with 'C-' followed by at least three digits)
     * @param cropDTO the updated crop data
     * @return ResponseEntity indicating the result of the update operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updateCrop(
            @Pattern(regexp = "C-\\d{3,}", message = "ID must start with 'C-' followed by at least three digits (e.g., C-001)")
            @PathVariable("id") String cropCode,
            @Valid @ModelAttribute CropCreateDTO cropDTO) {
        try {
            log.info("Attempting to update crop with ID: {}", cropCode);
            cropDTO.setCropCode(cropCode);
            cropService.updateCrop(cropDTO);
            log.info("Successfully updated crop: {}", cropDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CropNotFoundException e) {
            log.warn("Crop not found for ID: {}", cropCode);
            return new ResponseEntity<>(
                    ResponseObj.builder().code(404).message("Crop not found.")
                    .build(),HttpStatus.NOT_FOUND);
        } catch (DataPersistFailedException e) {
            log.error("Failed to update crop: {}", cropDTO, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Deletes an existing crop by ID.
     *
     * @param cropCode the ID of the crop to be deleted (must start with 'C-' followed by at least three digits)
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCrop(
            @Pattern(regexp = "C-\\d{3,}", message = "ID must start with 'C-' followed by at least three digits (e.g., C-001)")
            @PathVariable("id") String cropCode) {
        try {
            log.info("Attempting to delete crop with ID: {}", cropCode);
            cropService.deleteCrop(cropCode);
            log.info("Successfully deleted crop with ID: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CropNotFoundException e) {
            log.warn("Crop not found for ID: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a crop by its ID.
     *
     * @param cropCode the ID of the crop to be retrieved (must start with 'C-' followed by at least three digits)
     * @return ResponseEntity containing the CropDTO if found, or an appropriate HTTP status if not
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CropDTO> findCrop(
            @Pattern(regexp = "C-\\d{3,}", message = "ID must start with 'C-' followed by at least three digits (e.g., C-001)")
            @PathVariable("id") String cropCode) {
        try {
            log.info("Attempting to find crop with ID: {}", cropCode);
            CropDTO cropDto = cropService.findCropById(cropCode);
            log.info("Successfully retrieved crop with ID: {}", cropCode);
            return new ResponseEntity<>(cropDto, HttpStatus.OK);
        } catch (CropNotFoundException e) {
            log.warn("Crop not found for ID: {}", cropCode);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Retrieves a list of all crops.
     *
     * @return ResponseEntity containing a list of CropDTOs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllCrops(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        log.info("Fetching all crops.");
        List<CropDTO> allCrops = cropService.findAllCrops(page, size);
        log.info("Successfully retrieved {} crops.", allCrops.size());
        return new ResponseEntity<>(allCrops, HttpStatus.OK);
    }
    /**
     * Retrieves a list of crop based on custom filter criteria.
     *
     * @param filterDTO the filter criteria encapsulated in a custom object
     * @return ResponseEntity containing the filtered list of crop
     */
    @PostMapping(
            value = "/filter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CropDTO>> filterField(@RequestBody CropFilterDTO filterDTO) {
        log.info("Attempting to filter crops with criteria: {}", filterDTO);
        try {
            List<CropDTO> crops = cropService.filterCrops(filterDTO);
            log.info("Successfully filtered crops. Found {} results.", crops.size());
            return new ResponseEntity<>(crops, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to filter crop", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Retrieves a list of crops based on the field ID.
     *
     * @param fieldId the ID of the field to retrieve crops for
     * @return ResponseEntity containing the list of crops
     */
    @GetMapping(value = "/field/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CropDTO>> findCropsOfField(
            @Pattern(regexp = "F-\\d{3,}", message = "ID must start with 'F-' followed by at least three digits (e.g., F-001)")
            @PathVariable("id") String fieldId) {
        try {
            log.info("Attempting to retrieve crops for field with ID: {}", fieldId);
            List<CropDTO> cropDtos = cropService.findCropsOfField(fieldId);
            log.info("Successfully retrieved {} crops for field with ID: {}", cropDtos.size(), fieldId);
            return new ResponseEntity<>(cropDtos, HttpStatus.OK);
        } catch (FieldNotFoundException e) {
            log.warn("Field not found for ID: {}", fieldId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
