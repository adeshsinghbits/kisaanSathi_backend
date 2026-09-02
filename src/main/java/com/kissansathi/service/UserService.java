package com.kissansathi.service;

import com.kissansathi.dto.user.ProfileImageRequest;
import com.kissansathi.dto.user.UpdateProfileRequest;
import com.kissansathi.dto.user.UserProfileResponse;

public interface UserService {
    UserProfileResponse getMyProfile();
    UserProfileResponse updateMyProfile(UpdateProfileRequest request);
    UserProfileResponse updateProfileImage(ProfileImageRequest request);
    void deleteMyAccount();
}