package com.kissansathi.service;

import com.kissansathi.dto.auth.AuthResponse;
import com.kissansathi.dto.auth.LoginRequest;
import com.kissansathi.dto.auth.RegisterRequest;
import com.kissansathi.dto.user.UserProfileResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(String refreshToken);
    void logout(String refreshToken);
    UserProfileResponse getCurrentUserProfile();
}