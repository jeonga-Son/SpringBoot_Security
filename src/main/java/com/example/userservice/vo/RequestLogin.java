package com.example.userservice.vo;

import lombok.Data;

@Data
public class RequestLogin { // 로그인 한 유저들의 정보만 얻기 위한 클래스
    private String email;

    private String password;
}
