package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDashboardDTO implements SuperDTO {
    private String fCode;
    private String fieldName;
    private Double fieldSize;
    private String fieldImage1;
    private int staffList;
    private int cropList;
}
