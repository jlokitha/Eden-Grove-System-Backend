package lk.ijse.Green_Shadow_Backend.repository;

import lk.ijse.Green_Shadow_Backend.entity.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
