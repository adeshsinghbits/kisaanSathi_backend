package com.kissansathi.dto.auth;

import com.kissansathi.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Minimal user info embedded in auth responses. Never contains the password hash. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
}