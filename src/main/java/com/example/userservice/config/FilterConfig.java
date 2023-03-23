package com.example.userservice.config;

import com.example.userservice.filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration // 설정파일 역할
public class FilterConfig {
    @Autowired
    private Environment env;

    @Bean
    // 클라이언트가 토큰을 가져왔는지 체크
    public FilterRegistrationBean<MyFilter> filter1() { // 필터 등록
        FilterRegistrationBean<MyFilter> bean =
                new FilterRegistrationBean<>(new MyFilter(env));

        // 필터를 누가 거치게 할 것인지? 어떤 대상?
        bean.addUrlPatterns("/users2/*");
        bean.addUrlPatterns("/hello/*");
        bean.setOrder(0); // bean을 최우선으로 적용한다.

        return bean;
    }
}
