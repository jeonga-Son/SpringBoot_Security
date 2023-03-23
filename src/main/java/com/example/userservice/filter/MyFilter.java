package com.example.userservice.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {
    private Environment env;

    public MyFilter(Environment env) { // application.yml 가져오기 위한 것
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest request2, ServletResponse response2, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터 호출");

        HttpServletRequest request = (HttpServletRequest) request2;
        HttpServletResponse response = (HttpServletResponse) response2;

        if(request.getHeader("AUTHORIZATION") == null) {
            onError(response, "UNAUTHORIZATION");
        } else {
            String authorizationHeader = request.getHeader("AUTHORIZATION");
            System.out.println(authorizationHeader);

            String jwt = authorizationHeader.replace("Bearer", ""); // Bearer 값이 있다면 지워라.

            if(!isJwtValid(jwt)) {
                onError(response, "UNAUTHORIZATION2");
            }
        }

        filterChain.doFilter(request2, response2); // 이렇게 함으로써 체인이 끊어지지 않고 필터를 연결해서 사용할 수 있다.
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()) { // 토큰이 제대로 들어가져있는지 체크
            returnValue = false;
        }

        return returnValue;
    }

    private void onError(HttpServletResponse response, String httpStatus) throws IOException {
        response.addHeader("error", httpStatus);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, httpStatus); // 400 error
    }
}
