package lk.ijse.Green_Shadow_Backend.dto.impl;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class MonitoringLogCreateDTO implements SuperDTO {
    @Pattern(regexp = "^M-\\d{3}$", message = "logCode must start with 'M-' followed by at least three digits (e.g., M-001).")
    private String logCode;
    @NotNull(message = "Observation cannot be null.")
    @Size(max = 500, message = "Observation must be at most 500 characters.")
    private String observation;
    @NotNull(message = "Observed image cannot be null.")
    private MultipartFile observedImage;
    @NotNull(message = "Fields list cannot be null.")
    @Pattern(regexp = "^F-\\d{3}$", message = "fieldCode must start with 'F-' followed by at least three digits (e.g., F-001).")
    private String fieldCode;
    @NotNull(message = "Crops list cannot be null.")
    @Size(min = 1, message = "At least one crop must be present.")
    private List<String> crops;
    @NotNull(message = "Staffs list cannot be null.")
    @Size(min = 1, message = "At least one staff member must be present.")
    private List<String> staffs;
}
