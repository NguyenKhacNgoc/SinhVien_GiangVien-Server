package com.example.sv.Controller.Component.CoVan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.Entity.GiangVien;
import com.example.sv.Repository.GiangVienRespository;
import com.example.sv.Repository.LopHCRepository;

import com.example.sv.Repository.SinhVienRepository;

@RestController
@RequestMapping("/api/covan")
public class CoVanController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LopHCRepository lopHCRepository;
    @Autowired
    private GiangVienRespository giangVienRespository;
    @Autowired
    private SinhVienRepository sinhVienRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            GiangVien cv = giangVienRespository.findByEmail(email).get();
            return ResponseEntity.ok(cv);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

    @GetMapping("/getlopHC")
    public ResponseEntity<?> getlopHC(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            GiangVien cv = giangVienRespository.findByEmail(email).get();
            return ResponseEntity.ok(lopHCRepository.findByCovan(cv));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

    @GetMapping("/getsvbylophc")
    public ResponseEntity<?> getsvbylophc(@RequestHeader("Authorization") String authorization,
            @RequestParam Long lophc) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            if (lopHCRepository.findById(lophc).isPresent()) {
                return ResponseEntity.ok(sinhVienRepository.findByLopHC(lopHCRepository.findById(lophc).get()));
            } else {
                return ResponseEntity.badRequest().body("Lớp hành chính không tồn tại");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

}
