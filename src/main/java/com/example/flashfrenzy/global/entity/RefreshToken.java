package com.example.flashfrenzy.global.entity;

import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken {

    private String username;
    private UserRoleEnum role;

    private Long Key;

    public RefreshToken(String username, UserRoleEnum role, Long key) {
        this.username = username;
        this.role = role;
        this.Key = key;
    }
}
