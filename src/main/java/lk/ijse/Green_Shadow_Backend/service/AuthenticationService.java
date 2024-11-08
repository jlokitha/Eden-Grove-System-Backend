package lk.ijse.Green_Shadow_Backend.service;

import lk.ijse.Green_Shadow_Backend.dto.impl.UserCreateDTO;
import lk.ijse.Green_Shadow_Backend.jwtmodels.JwtAuthResponse;
import lk.ijse.Green_Shadow_Backend.jwtmodels.SignIn;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse signUp(UserCreateDTO signUp);
    JwtAuthResponse refreshToken(String accessToken);
}
