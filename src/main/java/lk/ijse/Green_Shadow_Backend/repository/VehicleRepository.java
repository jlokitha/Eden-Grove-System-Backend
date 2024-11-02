package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findVehicleByLicensePlateNo(String licensePlateNo);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(v.vehicleCode, 3) AS int)), 0) FROM Vehicle v")
    int findLastIdNumber();
}
