package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    boolean existsStaffByEmail(String email);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(s.id, 3) AS int)), 0) FROM Staff s")
    int findLastIdNumber();
    Optional<Staff> findStaffByEmail (String email);
}
