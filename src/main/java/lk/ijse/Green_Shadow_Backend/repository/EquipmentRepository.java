package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Equipment;
import lk.ijse.Green_Shadow_Backend.enums.EquipmentType;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.equipmentId, 3) AS int)), 0) FROM Equipment e")
    int findLastIdNumber();
    @Query("SELECT e FROM Equipment e WHERE " +
            "(:type IS NULL OR e.type = :type) " +
            "AND (:status IS NULL OR e.status = :status)")
    List<Equipment> findAllByFilters(@Param("type") EquipmentType type, @Param("status") Status status, Pageable pageable);
}
