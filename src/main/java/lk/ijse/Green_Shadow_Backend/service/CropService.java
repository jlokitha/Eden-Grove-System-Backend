package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.CropCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.CropDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CropService {
    void saveCrop(CropCreateDTO cropDTO);
    void updateCrop(CropCreateDTO cropDTO);
    void deleteCrop(String cropId);
    CropDTO findCropById(String cropId);
    List<CropDTO> findAllCrops();
}
