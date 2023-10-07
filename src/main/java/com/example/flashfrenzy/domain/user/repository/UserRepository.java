package com.example.flashfrenzy.domain.user.repository;

import com.example.flashfrenzy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

}
