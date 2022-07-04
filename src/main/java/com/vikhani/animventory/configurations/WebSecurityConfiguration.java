package com.vikhani.animventory.configurations;

import com.vikhani.animventory.services.AppUserService;
import com.vikhani.animventory.components.LoginFailureHandler;
import com.vikhani.animventory.components.LoginSuccessHandler;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    CustomPasswordEncoder encoder;

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(encoder.encoder());

        return provider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/registration").not().authenticated()
                .and()
                    .authorizeRequests()
                        .anyRequest()
                        .authenticated()
                .and()
                    .formLogin()
                        .usernameParameter("username")
                        .failureHandler(loginFailureHandler)
                        .successHandler(loginSuccessHandler)
                .and()
                    .logout()
                        .permitAll()
                .and()
                    .httpBasic()
                .and()
                    .csrf()
                        .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserService);
    }
}
