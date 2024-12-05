package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterStaffDTO implements SuperDTO {
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, message = "First name must be at least 3 characters")
    @Pattern(regexp = "^[A-Za-z]{3,}$", message = "First name must only contain letters and have at least 3 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, message = "Last name must be at least 3 characters")
    @Pattern(regexp = "^[A-Za-z]{3,}$", message = "Last name must only contain letters and have at least 3 characters")
    private String lastName;
    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dob;
    @NotBlank(message = "Gender cannot be blank")
    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be 'MALE' or 'FEMALE'")
    private String gender;
    @NotBlank(message = "Designation cannot be blank")
    @Size(max = 30, message = "Designation must be at most 30 characters")
    private String designation;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email should be valid")
    private String email;
    @NotBlank(message = "Mobile cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be a valid phone number with exactly 10 digits")
    private String mobile;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "\\d{5}", message = "Postal code must be a valid 5-digit number")
    private String postalCode;
    private LocalDate joinedDate;
}
