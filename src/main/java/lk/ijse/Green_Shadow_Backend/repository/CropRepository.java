package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Crop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, String> {
    boolean existsCropByScientificName(String scientificName);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(c.cropCode, 3) AS int)), 0) FROM Crop c")
    int findLastIdNumber();
    @Query("SELECT c FROM Crop c WHERE c.status = 'AVAILABLE'")
    List<Crop> getAllCrop(Pageable pageable);
    @Query("SELECT c FROM Crop c WHERE " +
            "(:name IS NULL OR LOWER(c.commonName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(c.scientificName) LIKE LOWER(CONCAT('%', :name, '%'))) ")
    List<Crop> findAllByFilters(String name, Pageable pageable);
}
