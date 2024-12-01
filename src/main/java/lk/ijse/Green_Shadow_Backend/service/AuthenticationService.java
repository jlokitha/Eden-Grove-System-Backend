package lk.ijse.Green_Shadow_Backend.service;

import jakarta.mail.MessagingException;
import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.jwtmodels.JwtAuthResponse;
import lk.ijse.Green_Shadow_Backend.jwtmodels.SignIn;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthenticationService {
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse signUp(UserCreateDTO signUp);
    void resetUserPassword(UserCreateDTO userCreateDTO);
    void verifyUserEmail(String email) throws MessagingException, IOException;
    JwtAuthResponse refreshToken(String accessToken);
}
