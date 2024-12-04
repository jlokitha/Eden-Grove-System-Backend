package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.EquipmentDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.EquipmentFilterDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Equipment;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.enums.*;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.EquipmentNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.FieldNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.repository.EquipmentRepository;
import lk.ijse.Green_Shadow_Backend.repository.FieldRepository;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.service.EquipmentService;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final StaffRepository staffRepository;
    private final FieldRepository fieldRepository;
    private final Mapping mapping;
    @Override
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        Field field = getField(equipmentDTO.getField());
        Staff staff = getStaff(equipmentDTO.getStaff());
        try {
            Equipment equipment = mapping.convertToEntity(equipmentDTO, Equipment.class);
            equipment.setEquipmentId(
                    GenerateID.generateId(IdPrefix.EQUIPMENT.getPrefix(), (equipmentRepository.findLastIdNumber() + 1)));
            equipment.setType(EquipmentType.valueOf(equipmentDTO.getType()));

            equipment.setField(field);
            equipment.setStaff(staff);
            equipment.setStatus(Status.valueOf(equipmentDTO.getStatus()));
            equipmentRepository.save(equipment);
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to save equipment");
        }
    }
    @Override
    public void updateEquipment(EquipmentDTO equipmentDTO) {
        Field field = getField(equipmentDTO.getField());
        Staff staff = getStaff(equipmentDTO.getStaff());
        try {
            System.out.println(equipmentDTO);
            equipmentRepository.findById(equipmentDTO.getEquipmentId())
                    .ifPresentOrElse(
                            equipment -> {
                                equipment.setName(equipmentDTO.getName());
                                equipment.setType(EquipmentType.valueOf(equipmentDTO.getType()));
                                equipment.setField(field);
                                equipment.setStaff(staff);
                                equipment.setStatus(Status.valueOf(equipmentDTO.getStatus()));
                                equipmentRepository.save(equipment);
                            },
                            () -> {
                                throw new EquipmentNotFoundException("Equipment not found");
                            });

        } catch (EquipmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update equipment");
        }
    }
    @Override
    public void deleteEquipment(String equipmentId) {
        equipmentRepository.findById(equipmentId)
                .ifPresentOrElse(
                        equipment -> {
                            equipment.setStaff(null);
                            equipmentRepository.delete(equipment);
                        },
                        () -> {
                            throw new EquipmentNotFoundException("Equipment not found");
                        });
    }
    @Override
    public EquipmentDTO findEquipmentById(String equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .map(equipment -> {
                    EquipmentDTO equipmentDTO = mapping.convertToDTO(equipment, EquipmentDTO.class);
                    equipmentDTO.setStaffDTO(
                            equipment.getStaff() != null
                                    ? mapping.convertToDTO(equipment.getStaff(), StaffDTO.class)
                                    : null);
                    equipmentDTO.setFieldDTO(
                            equipment.getField() != null
                                    ? mapping.convertToDTO(equipment.getField(), FieldDTO.class)
                                    : null);
                    return equipmentDTO;
                })
                .orElseThrow(() -> new EquipmentNotFoundException("Equipment not found"));
    }
    @Override
    public List<EquipmentDTO> findAllEquipments(Integer page, Integer size) {
        return equipmentRepository.findAll(PageRequest.of(page, size, Sort.by( "name").descending()))
                .stream().map(equipment -> {
                    EquipmentDTO equipmentDTO = mapping.convertToDTO(equipment, EquipmentDTO.class);
                    equipmentDTO.setStaffDTO(null);
                    equipmentDTO.setFieldDTO(null);
                    return equipmentDTO;
                }).toList();
    }
    @Override
    public List<EquipmentDTO> filterAllEquipments(EquipmentFilterDTO filterDTO) {
        EquipmentType enumType = filterDTO.getType() != null ? EquipmentType.valueOf(filterDTO.getType().toUpperCase()) : null;
        Status enumStatus = filterDTO.getStatus() != null ? Status.valueOf(filterDTO.getStatus().toUpperCase()) : null;
        System.out.println("enumType = " + enumType);
        System.out.println("enumStatus = " + enumStatus);
        return equipmentRepository.findAllByFilters(
                enumType,
                enumStatus,
                PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), Sort.by("equipmentId").descending())
                )
                .stream().map(equipment -> {
                    EquipmentDTO equipmentDTO = mapping.convertToDTO(equipment, EquipmentDTO.class);
                    equipmentDTO.setStaffDTO(null);
                    equipmentDTO.setFieldDTO(null);
                    return equipmentDTO;
                }).toList();
    }
    private Staff getStaff(String staffId) {
        if (staffId == null) {
            return null;
        } else {
            return staffRepository.findById(staffId)
                    .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                    .orElseThrow(() -> new StaffNotFoundException("Staff not available"));
        }
    }
    private Field getField(String fieldId) {
        if (fieldId == null) {
            return null;
        } else {
            return fieldRepository.findById(fieldId)
                    .filter(field -> field.getStatus().equals(AvailabilityStatus.AVAILABLE))
                    .orElseThrow(() -> new FieldNotFoundException("Field not available"));
        }
    }
}
