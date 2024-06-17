package ru.dyakov.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.dyakov.requests.SignInRequest;
import ru.dyakov.responses.AuthResponse;

import java.util.Collection;
import java.util.Collections;

@Component
public class CustomRemoteAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        SignInRequest request = new SignInRequest(username, password);

        String authUrl = "http://localhost:8081/auth/signin";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(authUrl, request, AuthResponse.class);
            if (response.getBody() != null && response.getBody().isAuthenticated()) {
                Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
                return new JwtAuthentication(username, password, authorities, response.getBody().getToken());//new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
