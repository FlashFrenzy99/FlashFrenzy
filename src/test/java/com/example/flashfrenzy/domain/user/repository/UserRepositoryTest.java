package com.example.flashfrenzy.domain.user.repository;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @DisplayName("가상 유저 저장하기")
    void userInit() {
        userRepository.save(new User("test", "1234", UserRoleEnum.USER, "123@naver.com", new Basket()));
    }

    @Test
    @DisplayName("유저이름으로 저장된 유저 찾기")
    void findByUsername() {
        // goven
        String username = "test";

        // when
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다."));

        // then
        assertNotNull(user);
        assertEquals(user.getUsername(), "test");
    }

    @Test
    @DisplayName("저장되지 않은 유저이름으로 저장된 유저 찾기")
    void findByUsernameFail() {
        // goven
        String username = "test2";

        // when then
        assertThrows(IllegalArgumentException.class,() -> userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다.")), "해당 유저가 없습니다.");
    }

    @Test
    @DisplayName("이메일로 저장된 유저 찾기")
    void findByEmail() {
        // goven
        String email = "123@naver.com";

        // when
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다."));

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("123@naver.com");
    }

    @Test
    @DisplayName("저장되지 않은 이메일로 저장된 유저 찾기")
    void findByEmailFail() {
        // goven
        String email = "1234@maver.com";

        // when then
        assertThrows(IllegalArgumentException.class,() -> userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 없습니다.")), "해당 유저가 없습니다.");
    }
}
