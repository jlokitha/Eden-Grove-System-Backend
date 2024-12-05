package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleFilterDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VehicleService {
    void saveVehicle(VehicleDTO vehicleDTO);
    void updateVehicle(VehicleDTO vehicleDTO);
    void deleteVehicle(String vehicleId);
    VehicleDTO findVehicleById(String vehicleId);
    List<VehicleDTO> findAllVehicles(Integer page, Integer size);
    List<VehicleDTO> filterVehicle(VehicleFilterDTO filterDTO);
    int getVehicleCount();
}
