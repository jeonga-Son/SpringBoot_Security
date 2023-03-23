package com.example.userservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId); // 메소드명이 SQL 문으로 동작하도록 만들어준다

    UserEntity findByUserEmail(String username); // 여기서의 username은 email을 의미한다.
}
