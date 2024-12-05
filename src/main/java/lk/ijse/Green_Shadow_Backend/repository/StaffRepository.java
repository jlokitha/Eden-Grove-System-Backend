package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.enums.Designation;
import lk.ijse.Green_Shadow_Backend.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    boolean existsStaffByEmail(String email);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(s.id, 3) AS int)), 0) FROM Staff s")
    int findLastIdNumber();
    Optional<Staff> findStaffByEmail (String email);
    @Query("SELECT s FROM Staff s WHERE s.status = 'ACTIVE'")
    List<Staff> getAllStaff(Pageable pageable);
    @Query("SELECT s FROM Staff s WHERE s.status = 'ACTIVE' " +
            "AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:designation IS NULL OR s.designation = :designation) " +
            "AND (:gender IS NULL OR s.gender = :gender)")
    List<Staff> findAllByFilters(String name, Designation designation, Gender gender, Pageable pageable);
}
