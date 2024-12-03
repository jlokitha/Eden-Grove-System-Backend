package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CropService {
    void saveCrop(CropCreateDTO cropDTO);
    void updateCrop(CropCreateDTO cropDTO);
    void deleteCrop(String cropId);
    CropDTO findCropById(String cropId);
    List<CropDTO> findAllCrops(Integer page, Integer size);
    List<CropDTO> filterCrops(CropFilterDTO filterDTO);
    List<CropDTO> findCropsOfField(String fieldId);
}
