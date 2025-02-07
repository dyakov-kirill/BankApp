package ru.dyakov.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String phoneNumber;
    private String password;

    public SignUpRequest(String password, String phoneNumber) {
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
