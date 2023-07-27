package com.example.sv.Controller.Component.SinhVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.Entity.SinhVien;
import com.example.sv.Repository.DiemRepository;
import com.example.sv.Repository.SinhVienRepository;

@RestController
@RequestMapping("/api")
public class DiemController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private DiemRepository diemRepository;
    @Autowired
    private SinhVienRepository sinhVienRepository;

    @GetMapping("/getDiem")
    public ResponseEntity<?> getDiem(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            SinhVien sinhVien = sinhVienRepository.findByEmail(email).get();
            return ResponseEntity.ok(diemRepository.findBySinhVien(sinhVien));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

}
