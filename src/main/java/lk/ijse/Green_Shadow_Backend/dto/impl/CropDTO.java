package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropDTO implements SuperDTO {
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String category;
    private String season;
    private String status;
    private String cropImage;
    private FieldDTO fieldDto;
}