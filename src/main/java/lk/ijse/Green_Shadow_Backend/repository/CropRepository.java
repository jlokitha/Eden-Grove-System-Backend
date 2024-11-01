package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepository extends JpaRepository<Crop, String> {
    boolean existsCropByScientificName(String scientificName);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(f.fCode, 3) AS int)), 0) FROM Field f")
    int findLastIdNumber();
}
