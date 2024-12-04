package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogFilterDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.TopFieldsDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MonitoringLogService {
    void saveMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO);
    void updateMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO);
    MonitoringLogDTO findMonitoringLogById(String logCode);
    List<MonitoringLogDTO> findAllMonitoringLogs();
    List<MonitoringLogDTO> findAllMonitoringLogs(Integer page, Integer size);
    List<MonitoringLogDTO> filterMonitoringLogs(MonitoringLogFilterDTO filterDTO);
    List<TopFieldsDTO> getTopFields();
}
