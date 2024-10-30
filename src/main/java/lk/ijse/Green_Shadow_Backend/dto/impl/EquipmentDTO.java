package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lk.ijse.Green_Shadow_Backend.enums.EquipmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDTO implements SuperDTO {
    private String equipmentId;
    private String name;
    private EquipmentType type;
    private String status;
    private StaffDTO staff;
    private FieldDTO field;
}