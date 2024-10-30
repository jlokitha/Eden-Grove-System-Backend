package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, String> {
}
