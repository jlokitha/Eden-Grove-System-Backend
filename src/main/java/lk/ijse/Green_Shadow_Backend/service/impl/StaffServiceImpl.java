package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.RegisterStaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.EmailAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.service.StaffService;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final Mapping mapping;
    @Override
    public void saveStaff(RegisterStaffDTO dto) {
        if (staffRepository.existsStaffByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistException("Email is already exist");
        } else {
            try {
                Staff staff = mapping.convertToEntity(dto);
                staff.setId(GenerateID.generateId(
                        IdPrefix.STAFF.getPrefix(), staffRepository.findLastIdNumber())
                );
                staff.setStatus(StaffStatus.ACTIVE);
                staff.setName(dto.getFirstName() + " " + dto.getLastName());
                staff.setGender(Gender.valueOf(dto.getGender()));
                staff.setDesignation(Designation.valueOf(dto.getDesignation()));
                staff.setRole(Designation.valueOf(dto.getDesignation()).getRole());
                staff.setAddress(dto.getHouseNo() + ", " + dto.getLane() + ", " + dto.getCity() + ", " + dto.getState());
                staffRepository.save(staff);
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to save the staff");
            }
        }
    }
    @Override
    public void updateStaff(StaffDTO dto) {
        Staff staff = staffRepository.findById(dto.getId())
                .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                .orElseThrow(() -> new StaffNotFoundException("Staff not found"));
        if (!staff.getEmail().equals(dto.getEmail()) && staffRepository.existsStaffByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistException("Email is already in use");
        }
        try {
            staff.setName(dto.getName());
            staff.setDob(dto.getDob());
            staff.setGender(Gender.valueOf(dto.getGender()));
            staff.setDesignation(Designation.valueOf(dto.getDesignation()));
            staff.setRole(Designation.valueOf(dto.getDesignation()).getRole());
            staff.setEmail(dto.getEmail());
            staff.setMobile(dto.getMobile());
            staff.setAddress(dto.getAddress());
            staff.setPostalCode(dto.getPostalCode());
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update the staff");
        }
    }
    @Override
    public void deleteStaff(String id) {
        if (!staffRepository.existsById(id)) {
            throw new StaffNotFoundException("Staff not found");
        } else {
            try {
                staffRepository.findById(id)
                        .ifPresent(staff -> staff.setStatus(StaffStatus.DEACTIVATED));
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to delete the staff");
            }
        }
    }
    @Override
    public StaffDTO findStaffById(String id) {
        return staffRepository.findById(id)
                .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                .map(mapping::convertToDTO)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found"));
    }
    @Override
    public List<StaffDTO> findAllStaff() {
        return mapping.convertToDTO(
                staffRepository.findAll()
                        .stream()
                        .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                        .toList());
    }
}
