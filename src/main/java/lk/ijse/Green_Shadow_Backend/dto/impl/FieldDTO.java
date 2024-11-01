package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements SuperDTO {
    private String fCode;
    private String fieldName;
    private Double fieldSize;
    private String fieldLocation;
    private String image1;
    private String image2;
    private String status;
}
