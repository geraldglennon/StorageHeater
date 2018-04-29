package com.glennon.storageHeater.infrastructure.security.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static com.glennon.storageHeater.infrastructure.Profiles.LDAP;

@Configuration
@EnableWebSecurity
@Profile(LDAP)
public class LdapConfig extends SecurityConfig {

    @Value("${ldap.url}")
    private String url;

    @Value("${ldap.dnPattern}")
    private String dnPattern;

    @Value("${ldap.user}")
    private String user;

    @Value("${ldap.password}")
    private String password;

    @Value("${ldap.search.base}")
    private String searchBase;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns(dnPattern)
                .groupSearchBase(searchBase)
                .contextSource()
                .managerDn(user)
                .managerPassword(password)
                .url(url);
    }

    @Override
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setUserDn(user);
        ldapContextSource.setPassword(user);
        ldapContextSource.setBase(searchBase);
        ldapContextSource.afterPropertiesSet();
        return ldapContextSource;
    }
}
