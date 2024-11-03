package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.*;
import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.entity.impl.MonitoringLog;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.enums.AvailabilityStatus;
import lk.ijse.Green_Shadow_Backend.enums.IdPrefix;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.repository.CropRepository;
import lk.ijse.Green_Shadow_Backend.repository.FieldRepository;
import lk.ijse.Green_Shadow_Backend.repository.MonitoringLogRepository;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.service.MonitoringLogService;
import lk.ijse.Green_Shadow_Backend.utils.ConvertToBase64;
import lk.ijse.Green_Shadow_Backend.utils.GenerateID;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MonitoringLogServiceImpl implements MonitoringLogService {
    private final MonitoringLogRepository monitoringLogRepository;
    private final StaffRepository staffRepository;
    private final CropRepository cropRepository;
    private final FieldRepository fieldRepository;
    private final Mapping mapping;
    @Override
    public void saveMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO) {
        try {
            MonitoringLog monitoringLog = mapping.convertToEntity(monitoringLogCreateDTO, MonitoringLog.class);
            monitoringLog.setLogCode(GenerateID.generateId(
                    IdPrefix.MONITORING_LOG.getPrefix(), (monitoringLogRepository.findLastIdNumber() + 1)));
            monitoringLog.setLogDate(new Date());
            monitoringLog.setUpdatedDate(new Date());
            monitoringLog.setStaffs(getStaffs(monitoringLogCreateDTO.getStaffs()));
            monitoringLog.setField(getFields(monitoringLogCreateDTO.getFieldCode()));
            monitoringLog.setCrops(getCrops(monitoringLogCreateDTO.getCrops()));
            monitoringLog.setObservedImage(ConvertToBase64.toBase64Image(monitoringLogCreateDTO.getObservedImage()));
            monitoringLogRepository.save(monitoringLog);
        } catch (StaffNotFoundException | CropNotFoundException | FieldNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to save the monitoring log.");
        }
    }
    @Override
    public void updateMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO) {
        try {
            monitoringLogRepository.findById(monitoringLogCreateDTO.getLogCode())
                    .ifPresentOrElse(
                            monitoringLog -> {
                                monitoringLog.setUpdatedDate(new Date());
                                monitoringLog.setObservation(monitoringLogCreateDTO.getObservation());
                                monitoringLog.setStaffs(getStaffs(monitoringLogCreateDTO.getStaffs()));
                                monitoringLog.setCrops(getCrops(monitoringLogCreateDTO.getCrops()));
                                monitoringLog.setField(getFields(monitoringLogCreateDTO.getFieldCode()));
                                monitoringLog.setObservedImage(ConvertToBase64.toBase64Image(monitoringLogCreateDTO.getObservedImage()));
                            },
                            () -> {
                                throw new MonitoringLogNotFoundException("Monitoring log with code " + monitoringLogCreateDTO.getLogCode() + " is not found.");
                            }
                    );
        } catch (MonitoringLogNotFoundException | StaffNotFoundException | CropNotFoundException | FieldNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to update the monitoring log.");
        }
    }
    @Override
    public void deleteMonitoringLog(String logCode) {
        try {
            monitoringLogRepository.findById(logCode)
                    .ifPresentOrElse(
                            monitoringLogRepository::delete,
                            () -> {
                                throw new MonitoringLogNotFoundException("Monitoring log with code " + logCode + " is not found.");
                            }
                    );
        } catch (MonitoringLogNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to delete the monitoring log.");
        }
    }
    @Override
    public MonitoringLogDTO findMonitoringLogById(String logCode) {
        return monitoringLogRepository.findById(logCode)
                .map(monitoringLog -> {
                    MonitoringLogDTO monitoringLogDTO = mapping.convertToDTO(monitoringLog, MonitoringLogDTO.class);
                    monitoringLogDTO.setField(clearFieldImages(monitoringLogDTO.getField()));
                    monitoringLogDTO.setCrops(clearCropImages(monitoringLogDTO.getCrops()));
                    return monitoringLogDTO;
                })
                .orElseThrow(() -> new MonitoringLogNotFoundException("Monitoring log with code " + logCode + " is not found."));
    }
    @Override
    public List<MonitoringLogDTO> findAllMonitoringLogs() {
        return monitoringLogRepository.findAll()
                .stream().map(monitoringLog -> {
                    MonitoringLogDTO logDTO = mapping.convertToDTO(monitoringLog, MonitoringLogDTO.class);
                    logDTO.setField(clearFieldImages(logDTO.getField()));
                    logDTO.setCrops(clearCropImages(logDTO.getCrops()));
                    logDTO.setStaffs(null);
                    return logDTO;
                }).toList();
    }
    public List<Staff> getStaffs(List<String> staffCodes) {
        List<Staff> staffs = new ArrayList<>();
        staffCodes.forEach(staffCode -> {
            staffRepository.findById(staffCode)
                    .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                    .ifPresentOrElse(
                            staffs::add,
                            () -> {
                                throw new StaffNotFoundException("Staff member with code " + staffCode + " is not active.");
                            }
                    );
        });
        return staffs;
    }
    public List<Crop> getCrops(List<String> cropCodes) {
        List<Crop> crops = new ArrayList<>();
        cropCodes.forEach(cropCode -> {
            cropRepository.findById(cropCode)
                    .filter(c -> c.getStatus().equals(AvailabilityStatus.AVAILABLE))
                    .ifPresentOrElse(
                            crops::add,
                            () -> {
                                throw new CropNotFoundException("Crop with code " + cropCode + " is not found.");
                            }
                    );
        });
        return crops;
    }
    public Field getFields(String fieldCode) {
        return fieldRepository.findById(fieldCode)
                .filter(f -> f.getStatus().equals(AvailabilityStatus.AVAILABLE))
                .orElseThrow(() -> new FieldNotFoundException("Field with code " + fieldCode + " is not found."));
    }
    private FieldDTO clearFieldImages(FieldDTO fieldDTO) {
        fieldDTO.setImage1(null);
        fieldDTO.setImage2(null);
        return fieldDTO;
    }
    private List<CropDTO> clearCropImages(List<CropDTO> cropDTOs) {
        cropDTOs.forEach(crop -> crop.setCropImage(null));
        return cropDTOs;
    }
}
