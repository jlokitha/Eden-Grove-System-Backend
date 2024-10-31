package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.*;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO implements SuperDTO {
    @NotBlank(message = "ID cannot be blank")
    @Pattern(regexp = "S-\\d{3,}", message = "ID must start with 'S-' followed by at least three digits (e.g., S-001)")
    private String id;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]{3,}\\s[A-Za-z]{3,}$", message = "Name must contain exactly two words, each with at least three letters")
    private String name;
    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dob;
    @NotBlank(message = "Gender cannot be blank")
    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be 'MALE' or 'FEMALE'")
    private String gender;
    @NotBlank(message = "Designation cannot be blank")
    @Size(max = 30, message = "Designation must be at most 30 characters")
    private String designation;
    private String role;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email should be valid")
    private String email;
    @NotBlank(message = "Mobile cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be a valid phone number with exactly 10 digits")
    private String mobile;
    @NotBlank(message = "Address cannot be blank")
    @Pattern(
            regexp = "^[0-9]+\\s+[A-Za-z\\s]+,\\s+[A-Za-z\\s]+,\\s+[A-Za-z\\s]+$",
            message = "Address must include house number, lane name, city name, and state name"
    )
    private String address;
    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "\\d{5}", message = "Postal code must be a valid 5-digit number")
    private String postalCode;
    private Timestamp joinedDate;
}
