package com.glennon.storageHeater.infrastructure.security;

import com.glennon.storageHeater.infrastructure.security.login.HTTPAuthenticationEntryPoint;
import com.glennon.storageHeater.infrastructure.security.login.HTTPLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String LOGIN_URL = "/api/login";
    private final String[] API_ROOT = {"/**"};

    @Autowired
    private HTTPAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private HTTPLogoutSuccessHandler httpLogoutSuccessHandler;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(API_ROOT).anyRequest();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers(API_ROOT).permitAll();
                })
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilter(new JWTAuthenticationFilter(
                        authenticationConfiguration.getAuthenticationManager()))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling((exceptionHandler) -> {
                    exceptionHandler.authenticationEntryPoint(authenticationEntryPoint);
                })
                .logout((item) -> {
                    item.logoutRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, HttpMethod.DELETE.toString()))
                            .logoutSuccessHandler(httpLogoutSuccessHandler)
                            .permitAll();

                })
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new SecurityAuthenticationProvider();
    }

}
