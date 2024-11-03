package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.MonitoringLogDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MonitoringLogService {
    void saveMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO);
    void updateMonitoringLog(MonitoringLogCreateDTO monitoringLogCreateDTO);
    void deleteMonitoringLog(String logCode);
    MonitoringLogDTO findMonitoringLogById(String logCode);
    List<MonitoringLogDTO> findAllMonitoringLogs();
}
