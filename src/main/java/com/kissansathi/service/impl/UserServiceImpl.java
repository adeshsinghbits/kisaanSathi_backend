package com.kissansathi.service.impl;

import com.kissansathi.dto.user.ProfileImageRequest;
import com.kissansathi.dto.user.UpdateProfileRequest;
import com.kissansathi.dto.user.UserProfileResponse;
import com.kissansathi.entity.User;
import com.kissansathi.exception.ConflictException;
import com.kissansathi.repository.UserRepository;
import com.kissansathi.security.SecurityUtils;
import com.kissansathi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public UserProfileResponse getMyProfile() {
        return toDto(securityUtils.getCurrentUser());
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(UpdateProfileRequest request) {
        User user = securityUtils.getCurrentUser();

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            userRepository.findByPhone(request.getPhone()).ifPresent(existing -> {
                if (!existing.getId().equals(user.getId())) {
                    throw new ConflictException("This phone number is already in use");
                }
            });
            user.setPhone(request.getPhone());
        }
        // Note: role is intentionally never mutated here - farmers cannot self-promote.

        User saved = userRepository.save(user);
        log.info("User profile updated: userId={}", saved.getId());
        return toDto(saved);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfileImage(ProfileImageRequest request) {
        User user = securityUtils.getCurrentUser();
        user.setProfileImage(request.getProfileImage());
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteMyAccount() {
        User user = securityUtils.getCurrentUser();
        log.info("Deleting user account: userId={}", user.getId());
        userRepository.delete(user);
    }

    private UserProfileResponse toDto(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}