package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserDTO;
import lk.ijse.Green_Shadow_Backend.exception.DataPersistFailedException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotAcceptableException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotFoundException;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.repository.UserRepository;
import lk.ijse.Green_Shadow_Backend.service.JWTService;
import lk.ijse.Green_Shadow_Backend.service.UserService;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceIMPL implements UserService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final Mapping mapping;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    @Override
    public void updateUser(UserCreateDTO userCreateDTO, String token) {
        try {
            userRepository.findById(userCreateDTO.getEmail())
                    .ifPresentOrElse(
                            user -> {
                                if (!jwtService.extractUsername(token.substring(7)).equals(user.getEmail())) {
                                    throw new UserNotAcceptableException("User not acceptable");
                                } else {
                                user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
                                }
                            },
                            () -> {
                                throw new UserNotFoundException("User not found");
                            }
                    );
        } catch (UserNotFoundException | UserNotAcceptableException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("User update failed");
        }
    }
    @Override
    public void deleteUser(String userId) {
        try {
            userRepository.findById(userId)
                    .ifPresentOrElse(
                            userRepository::delete,
                            () -> {
                                throw new UserNotFoundException("User not found");
                            }
                    );
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataPersistFailedException("User delete failed");
        }
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserDTO userDTO = mapping.convertToDTO(user, UserDTO.class);
                    staffRepository.findStaffByEmail(user.getEmail())
                            .ifPresent(staff -> {
                                userDTO.setStaffId(staff.getId());
                                userDTO.setStaffName(staff.getName());
                                userDTO.setDesignation(String.valueOf(staff.getDesignation()));
                            });
                    return userDTO;
                }).toList();
    }
    @Override
    public UserDetailsService userDetailsService() {
        return email ->
                userRepository.findById(email)
                        .orElseThrow(()-> new UserNotFoundException("User Not found"));
    }
}
