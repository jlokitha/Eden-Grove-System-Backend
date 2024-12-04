package lk.ijse.Green_Shadow_Backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lk.ijse.Green_Shadow_Backend.customeObj.MailBody;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.entity.impl.Staff;
import lk.ijse.Green_Shadow_Backend.entity.impl.User;
import lk.ijse.Green_Shadow_Backend.enums.StaffStatus;
import lk.ijse.Green_Shadow_Backend.exception.*;
import lk.ijse.Green_Shadow_Backend.jwtmodels.JwtAuthResponse;
import lk.ijse.Green_Shadow_Backend.jwtmodels.SignIn;
import lk.ijse.Green_Shadow_Backend.repository.StaffRepository;
import lk.ijse.Green_Shadow_Backend.repository.UserRepository;
import lk.ijse.Green_Shadow_Backend.service.AuthenticationService;
import lk.ijse.Green_Shadow_Backend.service.JWTService;
import lk.ijse.Green_Shadow_Backend.utils.EmailUtil;
import lk.ijse.Green_Shadow_Backend.utils.Mapping;
import lk.ijse.Green_Shadow_Backend.utils.OtpManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceIMPL implements AuthenticationService {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final JWTService jwtService;
    private final Mapping mapping;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final OtpManager otpManager;

    @Override
    public JwtAuthResponse signUp(UserCreateDTO userCreateDTO) {
        if (otpManager.validateOtp(userCreateDTO.getEmail(), userCreateDTO.getOtp())) {
            otpManager.removeOtp(userCreateDTO.getEmail());
            userRepository.findById(userCreateDTO.getEmail())
                    .ifPresent(user -> {
                        throw new UserAlreadyExistException("User already exist");
                    });
            Staff staff = staffRepository.findStaffByEmail(userCreateDTO.getEmail())
                    .filter(s -> s.getStatus().equals(StaffStatus.ACTIVE))
                    .orElseThrow(() -> new StaffNotFoundException("Staff not found"));
            if (staff.getRole().equals("OTHER")) {
                throw new UserNotAcceptableException("Staff not found");
            }
            try {
                User user = mapping.convertToEntity(userCreateDTO, User.class);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole(staff.getRole());
                User save = userRepository.save(user);
                String token = jwtService.generateToken(save);
                return JwtAuthResponse.builder().token(token).build();
            } catch (Exception e) {
                throw new DataPersistFailedException("User registration failed");
            }
        } else throw new InvalidOtpException("Invalid OTP");
    }
    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
            UserDetails userByEmail = userRepository.findById(signIn.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            String generatedToken = jwtService.generateToken(userByEmail);
            return JwtAuthResponse.builder().token(generatedToken).build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
    @Override
    public void resetUserPassword(UserCreateDTO userCreateDTO) {
        if (otpManager.validateOtp(userCreateDTO.getEmail(), userCreateDTO.getOtp())) {
            otpManager.removeOtp(userCreateDTO.getEmail());
            userRepository.findById(userCreateDTO.getEmail())
                    .ifPresentOrElse(
                            user -> {
                                user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
                            },
                            () -> {
                                throw new UserNotFoundException("User not found");
                            }
                    );
        } else throw new InvalidOtpException("Invalid OTP");
    }
    @Override
    public void verifyUserEmail(String email) throws MessagingException, IOException {
        Staff staff = staffRepository.findStaffByEmail(email)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found"));
        String name = staff.getName();
        String subject = "Verify Your Email";
        String templateName = "AccountRegistrationVerification";
        if (userRepository.existsByEmail(email)) {
            subject = "Password Reset Request";
            templateName = "PasswordReset";
        }
        String otp = emailUtil.otpGenerator().toString();
        Map<String, String> map = new HashMap<>();
        map.put("username", name);
        map.put("otp code", otp);
        emailUtil.sendHtmlMessage(
                MailBody.builder()
                        .templateName(templateName)
                        .to(email)
                        .subject(subject)
                        .replacements(map)
                        .build()
        );
        otpManager.storeOtp(email, otp);
    }
    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        String userName = jwtService.extractUsername(accessToken);
        User user =
                userRepository.findById(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
        String refreshToken = jwtService.refreshToken(user);
        return JwtAuthResponse.builder().token(refreshToken).build();
    }
}
