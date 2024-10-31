package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    boolean existsStaffByEmail(String email);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(s.id, 3) AS int)), 1) FROM Staff s")
    int findLastIdNumber();
}
