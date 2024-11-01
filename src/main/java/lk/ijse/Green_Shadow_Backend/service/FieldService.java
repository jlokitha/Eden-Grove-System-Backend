package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.FieldAssociateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.FieldDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FieldService {
    void saveField(FieldCreateDTO fieldDTO);
    void updateField(FieldCreateDTO fieldDTO);
    void deleteField(String fieldId);
    FieldDTO findFieldById(String fieldId);
    List<FieldDTO> findAllFields();
    void updateFieldAssociate(FieldAssociateDTO fieldAssociateDTO);
}
