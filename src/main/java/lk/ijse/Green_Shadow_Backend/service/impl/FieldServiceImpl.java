package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

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
            field.setStaffs(fieldDTO.getStaffIds() != null ? getStaffList(fieldDTO.getStaffIds()) : null);
            List<Equipment> equipments = fieldDTO.getEquipmentCodes() != null ? getEquipmentList(fieldDTO.getEquipmentCodes()) : null;
            field.setEquipments(equipments);
            field.setStatus(AvailabilityStatus.AVAILABLE);
            Field save = fieldRepository.save(field);
            if (equipments != null) equipments.forEach(equipment -> equipment.setField(save));
        } catch (Exception e) {
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
                            field.setStaffs(fieldDTO.getStaffIds() != null ? getStaffList(fieldDTO.getStaffIds()) : null);
                            field.setEquipments(fieldDTO.getEquipmentCodes() != null ? getEquipmentList(fieldDTO.getEquipmentCodes()) : null);
                        },
                        () -> {
                            throw new FieldNotFoundException("Field not found");
                        }
                );
    }
    @Override
    public void deleteField(String fieldId) {
        fieldRepository.findById(fieldId)
                .ifPresentOrElse(
                        field -> {
                            field.setStatus(AvailabilityStatus.NOT_AVAILABLE);
                        },
                        () -> {
                            throw new FieldNotFoundException("Field not found");
                        }
                );
    }
    @Override
    public FieldDTO findFieldById(String fieldId) {
        return fieldRepository.findById(fieldId)
                .filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(field -> {
                    FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
                    fieldDTO.setFieldLocation(field.getFieldLocation().getX() + "," + field.getFieldLocation().getY());
                    fieldDTO.setFieldImage1(field.getFieldImage1());
                    fieldDTO.setFieldImage2(field.getFieldImage2());
                    fieldDTO.setCrops((field.getCrops() != null) ? getCropDtoList(field.getCrops()) : null);
                    fieldDTO.setStaffs((field.getStaffs() != null) ? getStaffDtoList(field.getStaffs()) : null);
                    fieldDTO.setEquipments((field.getEquipments() != null) ? getEquipmentDtoList(field.getEquipments()) : null);
                    return fieldDTO;
                })
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));
    }
    @Override
    public List<FieldDTO> findAllFields(int page, int size) {
       return fieldRepository.findAll(PageRequest.of(page, size, Sort.by("fCode").descending()))
                .stream()
               .filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(field -> {
                    FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
                    fieldDTO.setFieldLocation(field.getFieldLocation().getX() + "," + field.getFieldLocation().getY());
                    fieldDTO.setFieldImage1(field.getFieldImage1());
                    fieldDTO.setFieldImage2(field.getFieldImage2());
                    return fieldDTO;
                }).toList();
    }
    @Override
    public List<FieldDTO> findAllFields() {
        return fieldRepository.findAll(Sort.by("fCode").descending())
                .stream()
                .filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE)
                .map(field -> {
                    FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
                    fieldDTO.setFieldLocation(field.getFieldLocation().getX() + "," + field.getFieldLocation().getY());
                    fieldDTO.setFieldImage1(field.getFieldImage1());
                    fieldDTO.setFieldImage2(field.getFieldImage2());
                    return fieldDTO;
                }).toList();
    }
    @Override
    public List<FieldDTO> filterFields(FieldFilterDTO filterDTO) {
        String nameFilter = filterDTO.getName() != null ? filterDTO.getName().toLowerCase() : null;
        Double fromSize = filterDTO.getFromSize() != null ? filterDTO.getFromSize() : null;
        Double toSize = filterDTO.getToSize() != null ? filterDTO.getToSize() : null;
        List<Field> filteredFields = fieldRepository.findAllByFilters(
                nameFilter,
                fromSize,
                toSize,
                PageRequest.of(
                        filterDTO.getPage(),
                        filterDTO.getSize(),
                        Sort.by("fCode").descending())
        ).stream().filter(field -> field.getStatus() == AvailabilityStatus.AVAILABLE).toList();
        List<FieldDTO> fieldDTOS = new ArrayList<>();
        filteredFields.forEach(field -> {
            FieldDTO fieldDTO = mapping.convertToDTO(field, FieldDTO.class);
            fieldDTO.setFieldLocation(field.getFieldLocation().getX() + "," + field.getFieldLocation().getY());
            fieldDTO.setFieldImage1(field.getFieldImage1());
            fieldDTO.setFieldImage2(field.getFieldImage2());
            fieldDTOS.add(fieldDTO);
        });
        return fieldDTOS;
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
    private List<CropDTO> getCropDtoList(List<Crop> crops) {
        return crops.stream()
                .map(crop -> {
                    CropDTO cropDTO = mapping.convertToDTO(crop, CropDTO.class);
                    cropDTO.setCropImage(null);
                    return cropDTO;
                }).toList();
    }
    private List<StaffDTO> getStaffDtoList(List<Staff> staffs) {
        return staffs.stream()
                .map(staff -> {
                    return mapping.convertToDTO(staff, StaffDTO.class);
                }).toList();
    }
    private List<EquipmentDTO> getEquipmentDtoList(List<Equipment> equipments) {
        return equipments.stream()
                .map(e -> {
                    return mapping.convertToDTO(e, EquipmentDTO.class);
                }).toList();
    }
    private List<Equipment> getEquipmentList(List<String> equipmentCodes) {
        List<Equipment> equipments = new ArrayList<>();
        for (String equipmentCode : equipmentCodes) {
            equipmentRepository.findById(equipmentCode)
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
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        return new Point(x, y);
    }
}
