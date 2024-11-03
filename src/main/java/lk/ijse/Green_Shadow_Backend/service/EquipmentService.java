package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.EquipmentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EquipmentService {
    void saveEquipment(EquipmentDTO equipmentDTO);
    void updateEquipment(EquipmentDTO equipmentDTO);
    void deleteEquipment(String equipmentId);
    EquipmentDTO findEquipmentById(String equipmentId);
    List<EquipmentDTO> findAllEquipments();
}
