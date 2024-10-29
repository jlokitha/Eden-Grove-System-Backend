package lk.ijse.Green_Shadow_Backend.dto.impl;

import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterStaffDTO implements SuperDTO {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Gender gender;
    private Designation designation;
    private String email;
    private String mobile;
    private String houseNo;
    private String lane;
    private String city;
    private String state;
    private String postalCode;
}