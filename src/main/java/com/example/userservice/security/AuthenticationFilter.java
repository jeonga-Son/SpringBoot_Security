package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter { // 인증과 관련된 내용
    private UserService userService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    // 로그인 하기 위한 준비 과정. 인증 되기 전에 사전에 처리할 작업들을 호출하기 위해서 사용한다.
    // RequestLogin 객체로 변환하는 것을 담당
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(),
                    RequestLogin.class); // 요청된 데이터값(email, password 등..)을 입출력 스트림을 통해서 RequestLogin으로 변환해준다.

            return getAuthenticationManager().authenticate( // authenticate 메소드가 Authentication 객체로 리턴해준다.
                    new UsernamePasswordAuthenticationToken( // 토큰형태로 만들어서 Authentication객체로 리턴
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // 인증을 성공하고 난 후 호출되는 메소드
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername();

        UserDto userDetail = userService.getUserDetailByEmail(userName); // Server는 이 정보를 가지고 토큰을 만들어야 한다.

        String token = Jwts.builder()
                .setSubject(userDetail.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + // 현재 날짜보다 + 1 => 토큰 유효기간
                        Long.parseLong(env.getProperty("token.expiration_time")))) // yml 파일에 입력한 내용
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret")) // yml 파일에 입력한 내용
                .compact(); // 토큰이 만들어진다.

        // 클라이언트에게 토큰 및 유저 정보 보내기
        response.addHeader("token", token);
        response.addHeader("userId", userDetail.getUserId());

    }
}
