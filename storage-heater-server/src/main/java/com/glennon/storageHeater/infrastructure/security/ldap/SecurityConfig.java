package com.glennon.storageHeater.infrastructure.security.ldap;

import com.glennon.storageHeater.infrastructure.security.login.HTTPAuthenticationEntryPoint;
import com.glennon.storageHeater.infrastructure.security.login.HTTPLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public abstract class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String LOGIN_URL = "/api/login";
    private final String[] API_ROOT = {"/**"};

    public abstract LdapContextSource ldapContextSource();

    @Autowired
    private HTTPAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private HTTPLogoutSuccessHandler httpLogoutSuccessHandler;


//    @Override
//    public void configure(WebSecurity http) throws Exception {
//        http.ignoring()
//                .antMatchers(LOGIN_URL)
//                .anyRequest()
//                .antMatchers(HttpMethod.GET);
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .anonymous().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, HttpMethod.DELETE.toString()))
                .logoutSuccessHandler(httpLogoutSuccessHandler)
                .and()
                .sessionManagement()
                .maximumSessions(1);

        http.authorizeRequests()
                .antMatchers(LOGIN_URL).permitAll()
                .antMatchers(API_ROOT).permitAll()
                .anyRequest().authenticated();
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }
}
