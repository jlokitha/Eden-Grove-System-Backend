package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotNull;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldAssociateDTO implements SuperDTO {
    private String fCode;
    @NotNull(message = "Crop codes cannot be null")
    private List<String> cropCodes;
    @NotNull(message = "Staff IDs cannot be null")
    private List<String> staffIds;
    @NotNull(message = "Staff IDs cannot be null")
    private List<String> equipmentCodes;
}
