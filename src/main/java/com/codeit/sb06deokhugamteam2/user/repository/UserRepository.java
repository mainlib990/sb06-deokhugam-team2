package com.codeit.sb06deokhugamteam2.user.repository;

import com.codeit.sb06deokhugamteam2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    //UserService에서 findAll()후 필터링으로 성능문제발생, db에서 효율적인 조회를위해 필드추가
    Optional<User> findByEmail(String email);
}