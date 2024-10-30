package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO implements SuperDTO {
    private String vehicleCode;
    private String licensePlateNo;
    private String category;
    private String fuelType;
    private Status status;
    private String remark;
    private StaffDTO staff;
}