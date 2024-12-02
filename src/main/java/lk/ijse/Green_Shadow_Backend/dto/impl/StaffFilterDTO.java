package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffFilterDTO implements SuperDTO {
    private String name;
    private String designation;
    private String gender;
    private int page;
    private int size;
}
