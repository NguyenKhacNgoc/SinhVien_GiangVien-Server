package com.example.sv.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.Entity.User;
import com.example.sv.Repository.UserRepository;

@Service
public class AuthServices {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthServices(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public boolean authenticate(String email, String password, String role) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password) && user.get().getRole().equals(role)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String generateToken(String email) {
        return jwtTokenUtil.generateToken(email);
    }

}
