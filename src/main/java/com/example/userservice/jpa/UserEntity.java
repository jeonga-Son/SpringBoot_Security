package com.example.userservice.jpa;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "users2")
public class UserEntity { // Domain으로 보지 말고 JPA에서는 하나의 테이블로 봐야한다. 직접적으로 UserEntity를 사용하지 않는다.
    @Id
    @GeneratedValue(strategy = IDENTITY) // 값을 자동으로 증가시킨다.
    private Long id;

    @Column(nullable = false, unique = true) // null 값 허용x, 유니크한 값 => 제약조건 설정
    private String userId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String encrypedPwd;
}
