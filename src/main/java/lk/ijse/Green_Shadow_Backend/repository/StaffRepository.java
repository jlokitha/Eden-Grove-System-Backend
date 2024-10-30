package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
}
