package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.MonitoringLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringLogRepository extends JpaRepository<MonitoringLog, String> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(m.logCode, 3) AS int)), 0) FROM MonitoringLog m")
    int findLastIdNumber();
}
