package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldCreateDTO implements SuperDTO {
    private String fCode;
    @NotNull(message = "Field name cannot be null")
    @Size(min = 3, max = 100, message = "Field name must be between 3 and 100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Field name can only contain letters and spaces")
    private String fieldName;
    @NotNull(message = "Field size cannot be null")
    @Positive(message = "Field size must be a positive value")
    private Double fieldSize;
    @NotNull(message = "Field location cannot be null")
    @Pattern(regexp = "^-?\\d+\\s*,\\s*-?\\d+$", message = "Field location must be in the format 'x, y' where x and y are integers")
    private String fieldLocation;
    @NotNull(message = "Field image 1 cannot be null")
    private MultipartFile fieldImage1;
    @NotNull(message = "Field image 2 cannot be null")
    private MultipartFile fieldImage2;
    private List<String> cropCodes;
    private List<String> equipmentCodes;
    private List<String> staffIds;
}
