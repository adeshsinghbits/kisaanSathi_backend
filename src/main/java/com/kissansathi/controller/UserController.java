package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.user.ProfileImageRequest;
import com.kissansathi.dto.user.UpdateProfileRequest;
import com.kissansathi.dto.user.UserProfileResponse;
import com.kissansathi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Authenticated user's own profile management")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get my profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe() {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", userService.getMyProfile()));
    }

    @PutMapping("/me")
    @Operation(summary = "Update my full name / phone number")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMe(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", userService.updateMyProfile(request)));
    }

    @PatchMapping("/me/profile-image")
    @Operation(summary = "Update my profile image")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfileImage(@Valid @RequestBody ProfileImageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile image updated successfully", userService.updateProfileImage(request)));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete my account")
    public ResponseEntity<ApiResponse<Void>> deleteMe() {
        userService.deleteMyAccount();
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }
}