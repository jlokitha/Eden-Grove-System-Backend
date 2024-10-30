package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.MonitoringLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringLogRepository extends JpaRepository<MonitoringLog, String> {
}
