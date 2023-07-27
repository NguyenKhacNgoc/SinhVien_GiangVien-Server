package com.example.sv.Controller.Component.SinhVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;

import com.example.sv.Entity.SinhVien;
import com.example.sv.Repository.SinhVienRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SinhVienRepository sinhVienRepository;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        String accessToken = getTokenFromRequest(request);
        if (accessToken != null && jwtTokenUtil.validateToken(accessToken)) {
            String email = jwtTokenUtil.getEmailFromToken(accessToken);
            SinhVien sinhVien = sinhVienRepository.findByEmail(email).get();
            return ResponseEntity.ok(sinhVien);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
