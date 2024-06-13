package ru.dyakov.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {
    private String phoneNumber;
    private String password;

    public SignInRequest(String phoneNumber, String password) {
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
