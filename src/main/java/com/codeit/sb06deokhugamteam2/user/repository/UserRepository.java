package com.codeit.sb06deokhugamteam2.user.repository;

import com.codeit.sb06deokhugamteam2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    //UserService에서 findAll()후 필터링으로 성능문제발생, db에서 효율적인 조회를위해 필드추가
    Optional<User> findByEmail(String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)  //정상적인 물리삭제 작동위해 추가
    @Query(value = "delete FROM USERS WHERE id = :userId", nativeQuery = true) // JPQL, CASCADE 처리 주의
    void hardDeleteUserById(@Param("userId") UUID userId);
}