package com.example.sv.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.Entity.User;
import com.example.sv.Entity.UserAdmin;
import com.example.sv.Entity.UserCoVan;
import com.example.sv.Repository.UserAdminRespository;
import com.example.sv.Repository.UserCoVanRespository;
import com.example.sv.Repository.UserRepository;

@Service
public class AuthServices {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private UserCoVanRespository userCoVanRespository;

    private UserAdminRespository userAdminRespository;

    public AuthServices(UserRepository userRepository, JwtTokenUtil jwtTokenUtil,
            UserCoVanRespository userCoVanRespository, UserAdminRespository userAdminRespository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userCoVanRespository = userCoVanRespository;
        this.userAdminRespository = userAdminRespository;
    }

    public boolean authenticateSV(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean authenticateCV(String email, String password) {
        Optional<UserCoVan> user = userCoVanRespository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean authenticateAdmin(String email, String password) {
        Optional<UserAdmin> user = userAdminRespository.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
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
