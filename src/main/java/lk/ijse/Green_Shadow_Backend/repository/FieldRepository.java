package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, String> {
    Optional<Field> findFieldByFieldName(String fieldName);
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(f.fCode, 3) AS int)), 0) FROM Field f")
    int findLastIdNumber();
}
