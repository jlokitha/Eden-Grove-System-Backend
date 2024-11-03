package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.equipmentId, 3) AS int)), 0) FROM Equipment e")
    int findLastIdNumber();
}
