package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements SuperDTO {
    private String fCode;
    private String fieldName;
    private Double fieldSize;
    private String fieldLocation;
    private String fieldImage1;
    private String fieldImage2;
    private String status;
    private List<StaffDTO> staffs;
    private List<CropDTO> crops;
    private List<EquipmentDTO> equipments;
}
