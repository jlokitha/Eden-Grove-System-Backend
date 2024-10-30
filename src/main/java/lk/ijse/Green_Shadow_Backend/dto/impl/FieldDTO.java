package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements SuperDTO {
    private String fCode;
    private String fieldName;
    private Double fieldSize;
    private Point fieldLocation;
    private String fieldImage1;
    private String fieldImage2;
}