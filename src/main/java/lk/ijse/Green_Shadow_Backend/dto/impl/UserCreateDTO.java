package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO implements SuperDTO {
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email should be valid")
    private String email;
    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?=[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]*$).{6,}$",
            message = "Password must be at least 6 characters long, and contain at least one letter, one number, and one symbol. Only letters, numbers, and symbols are allowed.")
    private String password;
    @NotBlank(message = "Otp cannot be blank")
    @Pattern(regexp = "^\\d{6}$", message = "Otp must consist of exactly 6 numeric characters")
    private String otp;
}
