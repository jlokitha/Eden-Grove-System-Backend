package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.dto.impl.TopFieldsDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.MonitoringLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringLogRepository extends JpaRepository<MonitoringLog, String> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(m.logCode, 3) AS int)), 0) FROM MonitoringLog m")
    int findLastIdNumber();
    @Query("SELECT m FROM MonitoringLog m WHERE " +
            "(:name IS NULL OR LOWER(m.field.fieldName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:startOfDay IS NULL OR m.logDate >= :startOfDay) " +
            "AND (:endOfDay IS NULL OR m.logDate <= :endOfDay)")
    List<MonitoringLog> findAllByFilters(String name, Date startOfDay, Date endOfDay, Pageable pageable);
    @Query("SELECT m.field.fieldName, COUNT(m) AS logCount " +
            "FROM MonitoringLog m " +
            "GROUP BY m.field.fCode " +
            "ORDER BY logCount DESC")
    List<Object[]> findTopFieldsWithMostLogs(Pageable pageable);
}
