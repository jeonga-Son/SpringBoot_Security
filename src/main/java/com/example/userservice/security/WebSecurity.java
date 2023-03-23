package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    // Autowired 하니까 객체가 안넘어왔다. 문제가 되면 생성자로 만들어주기.
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한과 관련된 작업을 처리한다.
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users").permitAll(); // "/users"로 들어오는 것 들은 모두 허용.
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); // h2 db에 해당하는 내용 허용

        http.authorizeRequests().antMatchers("/**") // 특정 아이피로 들어오는 모든 요청에 대해서는 모두 필터링 처리를 해준다.
                        .hasIpAddress("localhost")
                                .and()
                                        .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable(); // h2 db와 관련된 것.

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception { // 이 메소드를 통해 AuthenticationFilter를 리턴해준다.
        AuthenticationFilter authenticationFilter
                = new AuthenticationFilter(authenticationManager());

        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증을 처리하기 위한 것.
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder); // 내가 넘겨주는 데이터값도 encrypt 한다.

    }
}









