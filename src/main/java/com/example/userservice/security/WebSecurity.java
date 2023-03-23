package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한과 관련된 작업을 처리한다.
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users").permitAll(); // "/users"로 들어오는 것 들은 모두 허용.
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); // h2 db에 해당하는 내용 허용

        http.headers().frameOptions().disable(); // h2 db와 관련된 것.
    }
}
