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
public class EquipmentDTO implements SuperDTO {
    @Pattern(regexp = "^E-\\d{3,}$", message = "Equipment ID must start with 'E-' followed by at least three digits (e.g., E-001)")
    private String equipmentId;
    @NotBlank(message = "Name must not be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    @NotBlank(message = "Type must not be empty")
    @Pattern(
            regexp = "MECHANICAL|ELECTRICAL|AGRICULTURAL|VEHICLE_ACCESSORY|OTHER",
            message = "Type must be one of the following: MECHANICAL, ELECTRICAL, AGRICULTURAL, VEHICLE_ACCESSORY, or OTHER"
    )
    private String type;
    @Pattern(
            regexp = "AVAILABLE|OUT_OF_SERVICE|IN_USE|UNDER_MAINTENANCE",
            message = "Status must be one of the following: AVAILABLE, OUT_OF_SERVICE, IN_USE, or UNDER_MAINTENANCE"
    )
    private String status;
    @Pattern(regexp = "^S-\\d{3}$", message = "Staff ID must start with 'S-' followed by three digits (e.g., S-001)")
    private String staff;
    @Pattern(regexp = "^F-\\d{3}$", message = "Field ID must start with 'F-' followed by three digits (e.g., F-001)")
    private String field;
}
