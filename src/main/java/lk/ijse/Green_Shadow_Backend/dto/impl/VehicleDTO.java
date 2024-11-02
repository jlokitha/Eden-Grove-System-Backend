package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO implements SuperDTO {
    private String vehicleCode;
    @NotBlank(message = "License plate number must not be empty")
    @Size(max = 20, message = "License plate number must be at most 20 characters")
    private String licensePlateNo;
    @NotBlank(message = "Category must not be empty")
    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;
    @NotBlank(message = "Fuel type must not be empty")
    @Size(max = 30, message = "Fuel type must be at most 30 characters")
    private String fuelType;
    @NotBlank(message = "Status must not be empty")
    @Pattern(regexp = "AVAILABLE|OUT_OF_SERVICE|IN_USE|UNDER_MAINTENANCE",
            message = "Status must be 'AVAILABLE', 'OUT_OF_SERVICE', 'IN_USE', or 'UNDER_MAINTENANCE'")
    private String status;
    @Size(max = 200, message = "Remark must be at most 200 characters")
    private String remark;
    @Pattern(regexp = "^S-\\d{3}$", message = "Staff ID must start with 'S-' followed by three digits (e.g., S-001)")
    private String staff;
}
