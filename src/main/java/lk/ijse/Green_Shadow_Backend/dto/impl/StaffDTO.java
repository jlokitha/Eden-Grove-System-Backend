package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lk.ijse.Green_Shadow_Backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO implements SuperDTO {
    private String id;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private Designation designation;
    private Role role;
    private String email;
    private String mobile;
    private String address;
    private String postalCode;
    private Timestamp joinedDate;
}