package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ijse.Green_Shadow_Backend.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropCreateDTO implements SuperDTO {
    private String cropCode;
    @NotBlank(message = "Common name must not be empty")
    @Size(min = 2, max = 100, message = "Common name must be between 2 and 100 characters")
    private String commonName;
    @NotBlank(message = "Scientific name must not be empty")
    @Size(min = 2, max = 150, message = "Scientific name must be between 2 and 150 characters")
    private String scientificName;
    @NotBlank(message = "Category must not be empty")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;
    @NotBlank(message = "Season must not be empty")
    @Size(min = 2, max = 30, message = "Season must be between 2 and 30 characters")
    private String season;
    @NotNull(message = "Crop image must not be null")
    private MultipartFile cropImage;
}
