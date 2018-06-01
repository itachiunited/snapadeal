package com.snapadeal.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Configuration

    public static class UserSecurityConfiguration extends WebSecurityConfigurerAdapter
    {
        public UserSecurityConfiguration()
        {
            super();
        }

        @Override
        protected void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll();
        }
    }
}
