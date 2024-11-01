package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldAssociateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import lk.ijse.Green_Shadow_Backend.entity.impl.Equipment;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.repository.CropRepository;
import lk.ijse.Green_Shadow_Backend.repository.EquipmentRepository;
import lk.ijse.Green_Shadow_Backend.repository.FieldRepository;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.service.FieldService;
import lk.ijse.Green_Shadow_Backend.utils.ConvertToBase64;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final StaffRepository staffRepository;
    private final CropRepository cropRepository;
    private final EquipmentRepository equipmentRepository;
    private final Mapping mapping;
    @Override
    public void saveField(FieldCreateDTO fieldDTO) {
        fieldRepository.findFieldByFieldName(fieldDTO.getFieldName())
                .ifPresent(field -> {
                    throw new FieldNameAlreadyExistException("Field name is already exist");
                });
        try {
            Field field = mapping.convertToEntity(fieldDTO, Field.class);
            field.setFCode(GenerateID.generateId(
                    IdPrefix.FIELD.getPrefix(), (fieldRepository.findLastIdNumber() + 1)));
            field.setFieldLocation(stringToPointConverter(fieldDTO.getFieldLocation()));
            field.setFieldImage1(ConvertToBase64.toBase64Image(fieldDTO.getFieldImage1()));
            field.setFieldImage2(ConvertToBase64.toBase64Image(fieldDTO.getFieldImage2()));
            field.setCrops(getCropList(fieldDTO.getCropCodes()));
            field.setStaffs(getStaffList(fieldDTO.getStaffIds()));
            field.setEquipments(getEquipmentList(fieldDTO.getEquipmentCodes()));
            field.setStatus(AvailabilityStatus.AVAILABLE);
            fieldRepository.save(field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataPersistFailedException("Failed to save the field");
        }
    }
    @Override
    public void updateField(FieldCreateDTO fieldDTO) {
        fieldRepository.findById(fieldDTO.getFCode())
                .ifPresentOrElse(
                        field -> {
                            field.setFieldName(fieldDTO.getFieldName());
                            field.setFieldSize(fieldDTO.getFieldSize());
                            field.setFieldLocation(stringToPointConverter(fieldDTO.getFieldLocation()));
                            field.setFieldImage1(ConvertToBase64.toBase64Image(fieldDTO.getFieldImage1()));
                            field.setFieldImage2(ConvertToBase64.toBase64Image(fieldDTO.getFieldImage2()));
                            field.setCrops(getCropList(fieldDTO.getCropCodes()));
                            field.setStaffs(getStaffList(fieldDTO.getStaffIds()));
                            field.setEquipments(getEquipmentList(fieldDTO.getEquipmentCodes()));
                        },
                        () -> {
                            throw new FieldNotFoundException("Field not found");
                        }
                );
    }
    @Override
    public void updateFieldAssociate(FieldAssociateDTO fieldAssociateDTO) {
        Field field = fieldRepository.findById(fieldAssociateDTO.getFCode())
                .filter(f -> f.getStatus() == AvailabilityStatus.AVAILABLE)
                .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldAssociateDTO.getFCode()));
        try {
            List<Crop> crops = getCropList(fieldAssociateDTO.getCropCodes());
            List<Staff> staffs = getStaffList(fieldAssociateDTO.getStaffIds());
            List<Equipment> equipments = getEquipmentList(fieldAssociateDTO.getEquipmentCodes());
            field.setCrops(crops);
            field.setStaffs(staffs);
            field.setEquipments(equipments);
            fieldRepository.save(field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataPersistFailedException("Failed to associate the field with crops and staffs");
        }
    }
    @Override
    public void deleteField(String fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));
        if (!field.getMonitoringLogs().isEmpty()) {
            field.setStatus(AvailabilityStatus.NOT_AVAILABLE);
            fieldRepository.save(field);
            throw new FieldDeletionException("Field cannot be deleted as it is referenced in MonitoringLog entries.");
        } else {
            try {
                field.getCrops().forEach(crop -> crop.setField(null));
                field.getStaffs().forEach(staff -> staff.getFields().remove(field));
                fieldRepository.delete(field);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataPersistFailedException("Failed to delete the field");
            }
        }

    }
    @Override
    public FieldDTO findFieldById(String fieldId) {
        return fieldRepository.findById(fieldId)
                .filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(field -> {
                    FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
                    fieldDTO.setFieldLocation(field.getFieldLocation().x + "," + field.getFieldLocation().y);
                    fieldDTO.setImage1(field.getFieldImage1());
                    fieldDTO.setImage2(field.getFieldImage2());
                    return fieldDTO;
                })
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));
    }
    @Override
    public List<FieldDTO> findAllFields() {
        return fieldRepository.findAll()
                .stream()
                .filter(f -> f.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(field -> {
                    FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
                    fieldDTO.setFieldLocation(field.getFieldLocation().x + "," + field.getFieldLocation().y);
                    fieldDTO.setImage1(field.getFieldImage1());
                    fieldDTO.setImage2(field.getFieldImage2());
                    return fieldDTO;
                }).toList();
    }
    private List<Staff> getStaffList(List<String> staffIds) {
        List<Staff> staffs = new ArrayList<>();
        for (String staffId : staffIds) {
            Staff staff = staffRepository.findById(staffId)
                    .filter(s -> s.getStatus() == StaffStatus.ACTIVE)
                    .orElseThrow(() -> new StaffNotFoundException("Active Staff not found with ID: " + staffId));
            staffs.add(staff);
        }
        return staffs;
    }
    private List<Crop> getCropList(List<String> cropCodes) {
        List<Crop> crops = new ArrayList<>();
        for (String cropCode : cropCodes) {
            Crop crop = cropRepository.findById(cropCode)
                    .filter(c -> c.getStatus() == AvailabilityStatus.AVAILABLE)
                    .orElseThrow(() -> new CropNotFoundException("Crop not found with ID: " + cropCode));
            crops.add(crop);
        }
        return crops;
    }
    private List<Equipment> getEquipmentList(List<String> equipmentCodes) {
        List<Equipment> equipments = new ArrayList<>();
        for (String equipmentCode : equipmentCodes) {
            equipmentRepository.findById(equipmentCode)
                    .filter(equipment -> equipment.getStatus() == Status.AVAILABLE)
                    .ifPresentOrElse(equipment -> {
                        equipment.setStatus(Status.IN_USE);
                        equipmentRepository.save(equipment);
                        equipments.add(equipment);
                    },
                    () -> {
                        throw new EquipmentNotFoundException("Equipment not found with ID: " + equipmentCode);
                    }
            );
        }
        return equipments;
    }
    public Point stringToPointConverter(String source) {
        String[] coords = source.split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        return new Point(x, y);
    }
}
