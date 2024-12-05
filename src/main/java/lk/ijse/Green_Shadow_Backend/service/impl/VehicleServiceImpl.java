package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.StaffDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.VehicleFilterDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.entity.impl.Vehicle;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.StaffNotFoundException;
import lk.ijse.Green_Shadow_Backend.exception.VehicleAlreadyExistException;
import lk.ijse.Green_Shadow_Backend.exception.VehicleNotFoundException;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.repository.VehicleRepository;
import lk.ijse.Green_Shadow_Backend.service.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final StaffRepository staffRepository;
    private final Mapping mapping;
    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleRepository.findVehicleByLicensePlateNo(vehicleDTO.getLicensePlateNo())
                .ifPresent(vehicle -> {
                    throw new VehicleAlreadyExistException("License plate number is already registered");
                });
        Staff staff = (vehicleDTO.getStaff() != null) ? staffRepository.findById(vehicleDTO.getStaff())
                .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                .orElseThrow(() -> new StaffNotFoundException("Active staff not found")) : null;
        try {
            Vehicle vehicle = mapping.convertToEntity(vehicleDTO, Vehicle.class);
            vehicle.setVehicleCode(GenerateID.generateId(
                    IdPrefix.VEHICLE.getPrefix(), (vehicleRepository.findLastIdNumber() + 1)));
            vehicle.setStaff(staff);
            vehicle.setStatus(Status.valueOf(vehicleDTO.getStatus()));
            vehicleRepository.save(vehicle);
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to save the vehicle");
        }
    }
    @Override
    public void updateVehicle(VehicleDTO vehicleDTO) {
        try {
            vehicleRepository.findById(vehicleDTO.getVehicleCode())
                    .ifPresentOrElse(vehicle -> {
                        vehicle.setLicensePlateNo(vehicleDTO.getLicensePlateNo());
                        vehicle.setCategory(vehicleDTO.getCategory());
                        vehicle.setFuelType(vehicleDTO.getFuelType());
                        vehicle.setStatus(Status.valueOf(vehicleDTO.getStatus()));
                        vehicle.setRemark(vehicleDTO.getRemark());
                        Staff staff = (vehicleDTO.getStaff() != null) ? staffRepository.findById(vehicleDTO.getStaff())
                                .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                                .orElseThrow(() -> new StaffNotFoundException("Active staff not found")) : null;
                        vehicle.setStaff(staff);
                        if (vehicle.getStatus().equals(Status.OUT_OF_SERVICE) || vehicle.getStatus().equals(Status.AVAILABLE)) {
                            vehicle.setStaff(null);
                        }
                    }, () -> {
                        throw new VehicleNotFoundException("Vehicle not found");
                    });
        } catch (VehicleNotFoundException | StaffNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update the vehicle");
        }
    }
    @Override
    public void deleteVehicle(String vehicleId) {
        vehicleRepository.findById(vehicleId)
                .ifPresentOrElse( vehicle -> {
                    vehicle.setStatus(Status.DELETED);
                    vehicle.setStaff(null);
                }, () -> {
                            throw new VehicleNotFoundException("Vehicle not found");
                        });
    }
    @Override
    public VehicleDTO findVehicleById(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    VehicleDTO vehicleDTO = mapping.convertToDTO(vehicle, VehicleDTO.class);
                    if (vehicle.getStaff() != null)
                        vehicleDTO.setStaffDTO(mapping.convertToDTO(vehicle.getStaff(), StaffDTO.class));
                    return vehicleDTO;
                })
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
    }
    @Override
    public List<VehicleDTO> findAllVehicles(Integer page, Integer size) {
        return vehicleRepository.findAll(PageRequest.of(page, size, Sort.by("vehicleCode").descending()))
                .stream()
                .filter(vehicle -> !vehicle.getStatus().equals(Status.DELETED))
                .map(vehicle -> {
                    VehicleDTO vehicleDTO = mapping.convertToDTO(vehicle, VehicleDTO.class);
                    vehicleDTO.setStaff(null);
                    return vehicleDTO;
                }).toList();
    }
    @Override
    public List<VehicleDTO> filterVehicle(VehicleFilterDTO filterDTO) {
        Status status = (filterDTO.getStatus() != null) ? Status.valueOf(filterDTO.getStatus()) : null;
        return vehicleRepository.findAllByFilters(
                filterDTO.getCategory(),
                status,
                        PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), Sort.by("vehicleCode").descending())
                )
                .stream()
                .filter(vehicle -> !vehicle.getStatus().equals(Status.DELETED))
                .map(vehicle -> {
                    VehicleDTO vehicleDTO = mapping.convertToDTO(vehicle, VehicleDTO.class);
                    vehicleDTO.setStaff(null);
                    return vehicleDTO;
                }).toList();
    }
    @Override
    public int getVehicleCount() {
        return vehicleRepository.findAll()
                .stream()
                .filter(vehicle -> !vehicle.getStatus().equals(Status.DELETED))
                .toList()
                .size();
    }
}
