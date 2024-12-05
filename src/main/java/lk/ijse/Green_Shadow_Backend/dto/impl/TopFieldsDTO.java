package lk.ijse.Green_Shadow_Backend.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopFieldsDTO {
    private String fieldName;
    private Long logCount;
}

