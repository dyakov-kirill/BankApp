package ru.dyakov.responses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private boolean isAuthenticated;

    public AuthResponse(String accessToken, String username, boolean isAuthenticated) {
        this.token = accessToken;
        this.username = username;
        this.isAuthenticated = isAuthenticated;
    }
}
