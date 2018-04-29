package com.glennon.storageHeater.infrastructure.security.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static com.glennon.storageHeater.infrastructure.Profiles.LDAP_EMBEDDED;


@EnableWebSecurity
@Profile(LDAP_EMBEDDED)
public class EmbeddedLdapConfig extends SecurityConfig {

    @Value("${ldap.url}")
    private String url;

    @Value("${ldap.dnPattern}")
    private String dnPattern;

    @Value("${ldap.ldif}")
    private String ldif;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns(dnPattern)
                .contextSource().ldif("classpath:" + ldif)
                .root("dc=springframework,dc=org");
    }

    @Override
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.afterPropertiesSet();
        return ldapContextSource;
    }
}
