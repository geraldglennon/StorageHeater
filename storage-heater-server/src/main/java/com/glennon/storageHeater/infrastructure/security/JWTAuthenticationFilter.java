package com.glennon.storageHeater.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glennon.storageHeater.infrastructure.models.JwtModel;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final AuthenticationManager authenticationManager = getAuthenticationManager();

        final Authentication authentication;
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            authentication = authenticationManager.authenticate(getDefaultUser());
        } else {
            authentication = authenticationManager.authenticate(readToken(httpServletRequest));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Authentication readToken(HttpServletRequest httpServletRequest) {
        final String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)
                .replace("Bearer ", "");
        if (StringUtils.isNotEmpty(token)) {
            try {
                final JWT jwt = JWTParser.parse(token);
                final JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
                final ObjectMapper objectMapper = new ObjectMapper();
                final JwtModel jwtModel = objectMapper.readValue(jwtClaimsSet.toString(true), JwtModel.class);

                final String username = jwtModel.getPreferredUsername();
                final List<GrantedAuthority> authorityList = new ArrayList<>();

                if (StringUtils.isNotEmpty(username)) {
                    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorityList);
                    final Map<String, Object> details = new HashMap<>();
                    final Optional<String> refreshToken = Optional.ofNullable(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
                    refreshToken.ifPresent(item -> details.put("refresh_token", item));

                }
            } catch(Exception e) {
                LOGGER.info("JWT failed to parse: {}", e.getMessage());
            }
        }
        return getDefaultUser();
    }

    private static UsernamePasswordAuthenticationToken getDefaultUser() {
        final List<GrantedAuthority> authorityList = new ArrayList<>();
        final Map<String, Object> details = new HashMap<>();
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken("default", null, authorityList);
        usernamePasswordAuthenticationToken.setDetails(details);
        return usernamePasswordAuthenticationToken;
    }
}
