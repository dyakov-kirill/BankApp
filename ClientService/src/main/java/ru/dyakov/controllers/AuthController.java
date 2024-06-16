package ru.dyakov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dyakov.entities.User;
import ru.dyakov.requests.SignInRequest;
import ru.dyakov.requests.SignUpRequest;
import ru.dyakov.responses.AuthResponse;
import ru.dyakov.responses.ValidateResponse;
import ru.dyakov.security.UserService;
import ru.dyakov.utils.JwtUtil;
import ru.dyakov.utils.PhoneNumberValidator;

@RestController
@Slf4j
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
        if (!PhoneNumberValidator.isValid(request.getPhoneNumber())) {
            return new ResponseEntity<>("Неверный формат номера телефона", HttpStatus.BAD_REQUEST);
        }
        if (userService.isUserExists(request)) {
            log.info("Пользователь с номером {} уже существует", request.getPhoneNumber());
            return new ResponseEntity<>("Пользователь уже существует", HttpStatus.BAD_REQUEST);
        } else {
            User user = userService.signUp(request);
            log.info("Пользователь с номером {} зарегестрирован", user.getPhoneNumber());
            return ResponseEntity.ok(user.getUsername());
        }
    }

    @PostMapping("signin")
    public ResponseEntity signIn(@RequestBody SignInRequest request){
        try {
            Authentication authenticated = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            log.info("Пользователь с номером {} вошел в систему", request.getPhoneNumber());
            User userDetails = (User) authenticated.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(
                    jwtToken,
                    userDetails.getUsername(),
                    true));
        } catch (AuthenticationException e) {
            log.error("Неудачная попытка входа");
            return new ResponseEntity<>("Неверный номер телефона или пароль", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("validateJwt")
    public ResponseEntity<ValidateResponse> validateJwt(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok(new ValidateResponse(jwtUtil.extractPhoneNumber(token)));
        } else {
            return ResponseEntity.badRequest().body(new ValidateResponse());
        }
    }

    @GetMapping("test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("OK");
    }
}
