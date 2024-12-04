package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Vehicle;
import lk.ijse.Green_Shadow_Backend.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findVehicleByLicensePlateNo(String licensePlateNo);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(v.vehicleCode, 3) AS int)), 0) FROM Vehicle v")
    int findLastIdNumber();
    @Query("SELECT v FROM Vehicle v WHERE " +
            "(:category IS NULL OR LOWER(v.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:status IS NULL OR v.status = :status)")
    List<Vehicle> findAllByFilters(String category, Status status, Pageable pageable);
}
