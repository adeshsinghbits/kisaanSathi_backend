package com.kissansathi.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImageRequest {

    @NotBlank(message = "Profile image URL/path is required")
    @jakarta.validation.constraints.Size(max = 255)
    private String profileImage;
}
