package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.FieldCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDashboardDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldFilterDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FieldService {
    void saveField(FieldCreateDTO fieldDTO);
    void updateField(FieldCreateDTO fieldDTO);
    void deleteField(String fieldId);
    FieldDTO findFieldById(String fieldId);
    List<FieldDTO> findAllFields(int page, int size);
    List<FieldDTO> findAllFields();
    List<FieldDTO> filterFields(FieldFilterDTO filterDTO);
    int getFieldCount();
    List<FieldDashboardDTO> getFieldListDashboard(int page, int size);
}
