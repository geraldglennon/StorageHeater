package com.glennon.storageHeater.presentation.services;

import com.glennon.storageHeater.presentation.api.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private UserSessionService userSessionService;

    private final Authentication anonymousUser = new AnonymousAuthenticationToken("key", "anonymousUser",
                                             AuthorityUtils.createAuthorityList("USER_ROLE"));

    public Authentication getCurrentUser() {
        Authentication auth = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .orElse(anonymousUser);

        return auth;
    }
}
