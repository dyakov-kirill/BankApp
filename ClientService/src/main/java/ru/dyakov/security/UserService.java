package ru.dyakov.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dyakov.entities.User;
import ru.dyakov.repositories.UserRepository;
import ru.dyakov.requests.SignUpRequest;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }

    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(user);
    }

    public boolean isUserExists(SignUpRequest signUpRequest) {
        return userRepository.findByPhoneNumber(signUpRequest.getPhoneNumber()).isPresent();
    }
}
