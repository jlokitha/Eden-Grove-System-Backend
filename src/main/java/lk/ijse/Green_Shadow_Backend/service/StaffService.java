package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.RegisterStaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffFilterDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffService {
    void saveStaff(RegisterStaffDTO dto);
    void updateStaff(StaffDTO dto);
    void deleteStaff(String id);
    StaffDTO findStaffById(String id);
    StaffDTO findStaffByToken(String token);
    List<StaffDTO> findAllStaff(int page, int size);

    List<StaffDTO> findAllStaff();

    List<StaffDTO> filterStaff(StaffFilterDTO filterDTO);
}
