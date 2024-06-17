package ru.dyakov.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if(!(authentication instanceof JwtAuthentication)) {
            return;
        }

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        request.getSession().setAttribute("Authorization", "Bearer " + jwtAuthentication.getToken());
        response.addHeader("Authorization", "Bearer " + jwtAuthentication.getToken());
        response.sendRedirect("/homepage");
    }
}
