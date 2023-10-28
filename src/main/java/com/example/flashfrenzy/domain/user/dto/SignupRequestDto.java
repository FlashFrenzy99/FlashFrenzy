package com.example.flashfrenzy.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {

    private String username;

    private String password;

    private String checkPassword;   // 패스워드 확인

    private String email;

    private String checkEmail;     // email로 보낸 코드

}
