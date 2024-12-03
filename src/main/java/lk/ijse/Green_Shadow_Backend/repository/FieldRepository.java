package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, String> {
    Optional<Field> findFieldByFieldName(String fieldName);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(f.fCode, 3) AS int)), 0) FROM Field f")
    int findLastIdNumber();
    @Query("SELECT f FROM Field f WHERE f.status = 'AVAILABLE'")
    List<Field> getAllField(Pageable pageable);
    @Query("SELECT f FROM Field f WHERE " +
            "(:name IS NULL OR LOWER(f.fieldName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:fromSize IS NULL OR f.fieldSize >= :fromSize) " +
            "AND (:toSize IS NULL OR f.fieldSize <= :toSize)")
    List<Field> findAllByFilters(String name, Double fromSize, Double toSize, Pageable pageable);
}
