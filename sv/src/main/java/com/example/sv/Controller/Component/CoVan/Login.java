package com.example.sv.Controller.Component.CoVan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.DTO.AuthResponse;
import com.example.sv.DTO.UserDTO;
import com.example.sv.Services.AuthServices;

@RestController
@RequestMapping("/api/covan")
public class Login {
    private final AuthServices authServices;

    public Login(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserDTO request) {

        if (authServices.authenticateCV(request.getEmail(), request.getPassword())) {
            String token = authServices.generateToken(request.getEmail());
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(authResponse);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
