package com.example.userservice.vo;

import lombok.Data;

@Data
public class ResponseUser { // 클라이언트가 봐야 할 내용들만 객체로 만들어서 response 해주는 용도.
    private String email;
    private String name;
    private String userId;
}
