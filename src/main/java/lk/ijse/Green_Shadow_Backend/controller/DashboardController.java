package lk.ijse.Green_Shadow_Backend.controller;

import lk.ijse.Green_Shadow_Backend.customeObj.DashboardCounts;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDashboardDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.TopFieldsDTO;
import lk.ijse.Green_Shadow_Backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DashboardController {
    private final StaffService staffService;
    private final VehicleService vehicleService;
    private final FieldService fieldService;
    private final CropService cropService;
    private final MonitoringLogService monitoringLogService;

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardCounts> countStaff() {
        int staffCount = staffService.getStaffCount();
        int vehicleCount = vehicleService.getVehicleCount();
        int fieldCount = fieldService.getFieldCount();
        int cropCount = cropService.getCropCount();
        return new ResponseEntity<>(
                DashboardCounts.builder()
                        .staffCount(staffCount)
                        .cropCount(cropCount)
                        .fieldCount(fieldCount)
                        .vehicleCount(vehicleCount)
                        .build(), HttpStatus.OK);
    }
    @GetMapping(value = "/field_list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDashboardDTO>> getFieldList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(fieldService.getFieldListDashboard(page, size), HttpStatus.OK);
    }
    @GetMapping(value = "/chart_data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TopFieldsDTO>> getChartData() {
        System.out.println("getChartData");
        return new ResponseEntity<>(monitoringLogService.getTopFields(), HttpStatus.OK);
    }
}
