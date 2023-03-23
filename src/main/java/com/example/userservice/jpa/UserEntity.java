package com.example.userservice.jpa;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "users2")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY) // 값을 자동으로 증가시킨다.
    private Long id;

    private String userId;

    private String email;

    private String name;

    private String password;
}
