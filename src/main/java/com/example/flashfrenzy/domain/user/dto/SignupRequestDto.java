package com.example.flashfrenzy.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String username;

    private String password;

    private String checkPassword;   // 패스워드 확인

    private String email;

    private String checkEmail;     // email로 보낸 코드

}
