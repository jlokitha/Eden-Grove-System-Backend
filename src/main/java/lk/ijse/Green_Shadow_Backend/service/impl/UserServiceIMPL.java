package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserFilterDTO;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserUpdateDTO;
import lk.ijse.Green_Shadow_Backend.exception.UserNotAcceptableException;
import lk.ijse.Green_Shadow_Backend.exception.UserNotFoundException;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.repository.UserRepository;
import lk.ijse.Green_Shadow_Backend.service.JWTService;
import lk.ijse.Green_Shadow_Backend.service.UserService;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public void updateUser(UserUpdateDTO dto, String token) {
        try {
            userRepository.findById(dto.getEmail())
                    .ifPresentOrElse(
                            user -> {
                                if (!jwtService.extractUsername(token.substring(7)).equals(user.getEmail())) {
                                    throw new UserNotAcceptableException("User not acceptable");
                                } else {
                                    user.setPassword(passwordEncoder.encode(dto.getPassword()));
                                }
                            },
                            () -> {
                                throw new UserNotFoundException("User not found");
                            }
                    );
        } catch (UserNotFoundException | UserNotAcceptableException e) {
            throw e;
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
        }
    }
    @Override
    public List<UserDTO> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
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
    public List<UserDTO> filterUser(UserFilterDTO filterDTO) {
        return userRepository.findAll(PageRequest.of(filterDTO.getPage(), filterDTO.getSize()))
                .stream()
                .filter(user -> user.getRole().equals(filterDTO.getRole()))
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
