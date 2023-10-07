package com.example.flashfrenzy.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    OWNER("ROLE_OWNER");

    private final String authority;
}
