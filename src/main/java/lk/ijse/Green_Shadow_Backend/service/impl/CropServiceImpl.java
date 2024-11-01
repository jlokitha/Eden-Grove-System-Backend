package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.CropCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.CropDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.repository.CropRepository;
import lk.ijse.Green_Shadow_Backend.service.CropService;
import lk.ijse.Green_Shadow_Backend.utils.ConvertToBase64;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CropServiceImpl implements CropService {
    private final CropRepository cropRepository;
    private final Mapping mapping;
    @Override
    public void saveCrop(CropCreateDTO cropDTO) {
        if (cropRepository.existsCropByScientificName(cropDTO.getScientificName())) {
            throw new CropAlreadyExistException("Crop with " + cropDTO.getScientificName() + " is already exist");
        } else {
            try {
                Crop crop = mapping.convertToEntity(cropDTO, Crop.class);
                crop.setCropCode(GenerateID.generateId(
                        IdPrefix.CROP.getPrefix(), (cropRepository.findLastIdNumber() + 1)));
                crop.setCropImage(ConvertToBase64.toBase64Image(cropDTO.getCropImage()));
                crop.setStatus(AvailabilityStatus.AVAILABLE);
                cropRepository.save(crop);
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to save the crop");
            }
        }
    }
    @Override
    public void updateCrop(CropCreateDTO cropDTO) {
        Crop crop = cropRepository.findById(cropDTO.getCropCode())
                .orElseThrow(() -> new CropNotFoundException("No crop exist with " + cropDTO.getCropCode()));
        try {
            crop.setCommonName(cropDTO.getCommonName());
            crop.setScientificName(cropDTO.getScientificName());
            crop.setCategory(cropDTO.getCategory());
            crop.setSeason(cropDTO.getSeason());
            crop.setCropImage(ConvertToBase64.toBase64Image(cropDTO.getCropImage()));
            cropRepository.save(crop);
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update the crop");
        }
    }
    @Override
    public void deleteCrop(String cropId) {
        Crop crop = cropRepository.findById(cropId)
                .orElseThrow(() -> new CropNotFoundException("No crop exist with " + cropId));
        if (!crop.getMonitoringLogs().isEmpty() || !(crop.getField() == null)) {
            crop.setStatus(AvailabilityStatus.NOT_AVAILABLE);
            cropRepository.save(crop);
            throw new CropDeletionException("Cannot delete the crop. It is already in use");
        } else {
            try {
                cropRepository.delete(crop);
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to delete the crop");
            }
        }
    }
    @Override
    public CropDTO findCropById(String cropId) {
        return cropRepository.findById(cropId)
                .filter(crop -> crop.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(crop -> mapping.convertToDTO(crop, CropDTO.class))
                .orElseThrow(() -> new CropNotFoundException("Crop not found"));
    }
    @Override
    public List<CropDTO> findAllCrops() {
        return cropRepository.findAll()
                .stream()
                .filter(c -> c.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(crop -> mapping.convertToDTO(crop, CropDTO.class))
                .toList();
    }
}
