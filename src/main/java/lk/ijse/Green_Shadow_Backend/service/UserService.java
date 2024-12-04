package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserFilterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void updateUser(UserCreateDTO userCreateDTO, String token);
    void deleteUser(String userId);
    List<UserDTO> getAllUsers(int page, int size);
    List<UserDTO> filterUser(UserFilterDTO filterDTO);
    UserDetailsService userDetailsService();
}
