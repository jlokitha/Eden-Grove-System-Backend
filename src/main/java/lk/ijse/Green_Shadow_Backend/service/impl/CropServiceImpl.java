package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.repository.CropRepository;
import lk.ijse.Green_Shadow_Backend.repository.FieldRepository;
import lk.ijse.Green_Shadow_Backend.service.CropService;
import lk.ijse.Green_Shadow_Backend.utils.ConvertToBase64;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CropServiceImpl implements CropService {
    private final CropRepository cropRepository;
    private final FieldRepository fieldRepository;
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
                Crop save = cropRepository.save(crop);
                if (cropDTO.getFCode() != null) {
                    Field field = fieldRepository.findById(cropDTO.getFCode()).orElse(null);
                    save.setField(field);
                }
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to save the crop");
            }
        }
    }
    @Override
    public void updateCrop(CropCreateDTO cropDTO) {
        Crop crop = cropRepository.findById(cropDTO.getCropCode())
                .orElseThrow(() -> new CropNotFoundException("No crop exist with " + cropDTO.getCropCode()));
        Field field = fieldRepository.findById(cropDTO.getFCode()).orElse(null);
        try {
            crop.setCommonName(cropDTO.getCommonName());
            crop.setScientificName(cropDTO.getScientificName());
            crop.setCategory(cropDTO.getCategory());
            crop.setSeason(cropDTO.getSeason());
            crop.setCropImage(ConvertToBase64.toBase64Image(cropDTO.getCropImage()));
            crop.setField(field);
            cropRepository.save(crop);
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update the crop");
        }
    }
    @Override
    public void deleteCrop(String cropId) {
        cropRepository.findById(cropId)
                .ifPresentOrElse(
                        crop -> {
                            crop.setStatus(AvailabilityStatus.NOT_AVAILABLE);
                        },
                        () -> {
                            throw new CropNotFoundException("Crop not found");
                        }
                );
    }
    @Override
    public CropDTO findCropById(String cropId) {
        return cropRepository.findById(cropId)
                .filter(crop -> crop.getStatus().equals(AvailabilityStatus.AVAILABLE))
                .map(crop -> {
                    CropDTO cropDTO = mapping.convertToDTO(crop, CropDTO.class);
                    FieldDTO fieldDTO = (crop.getField() != null) ? getFieldDTO(crop.getField()) : null;
                    cropDTO.setFieldDto(fieldDTO);
                    return cropDTO;
                })
                .orElseThrow(() -> new CropNotFoundException("Crop not found"));
    }
    @Override
    public List<CropDTO> findAllCrops(Integer page, Integer size) {
        return cropRepository.getAllCrop(PageRequest.of(page, size, Sort.by("cropCode").descending()))
                .stream()
                .map(crop -> mapping.convertToDTO(crop, CropDTO.class))
                .toList();
    }
    @Override
    public List<CropDTO> filterCrops(CropFilterDTO filterDTO) {
        String nameFilter = filterDTO.getName() != null ? filterDTO.getName().toLowerCase() : null;
        List<Crop> crops = cropRepository.findAllByFilters(
                nameFilter,
                PageRequest.of(
                        filterDTO.getPage(),
                        filterDTO.getSize(),
                        Sort.by("cropCode").descending())
        ).stream().filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE).toList();
        List<CropDTO> cropDtos = new ArrayList<>();
        crops.forEach(crop -> {
            CropDTO cropDTO = mapping.convertToDTO(crop, CropDTO.class);
            cropDTO.setFieldDto(null);
            cropDtos.add(cropDTO);
        });
        return cropDtos;
    }
    @Override
    public List<CropDTO> findCropsOfField(String fieldId) {
        fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));
        return cropRepository.findAll().stream()
                .filter(crop -> crop.getStatus().equals(AvailabilityStatus.AVAILABLE))
                .filter(crop -> crop.getField() != null && crop.getField().getFCode().equals(fieldId))
                .map(crop -> {
                    CropDTO cropDTO = mapping.convertToDTO(crop, CropDTO.class);
                    cropDTO.setFieldDto(null);
                    return cropDTO;
                }).toList();
    }
    @Override
    public int getCropCount() {
        return cropRepository.findAll()
                .stream()
                .filter(crop -> crop.getStatus().equals(AvailabilityStatus.AVAILABLE))
                .toList().size();
    }
    private FieldDTO getFieldDTO(Field field) {
        FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
        fieldDTO.setFieldImage1(null);
        fieldDTO.setFieldImage2(null);
        fieldDTO.setCrops(null);
        fieldDTO.setEquipments(null);
        fieldDTO.setStaffs(null);
        return fieldDTO;
    }
}
