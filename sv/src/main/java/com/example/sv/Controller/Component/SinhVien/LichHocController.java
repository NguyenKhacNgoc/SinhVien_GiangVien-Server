package com.example.sv.Controller.Component.SinhVien;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.Entity.LichHoc;
import com.example.sv.Entity.LopTC;
import com.example.sv.Entity.SinhVien;
import com.example.sv.Repository.LichHocRepository;
import com.example.sv.Repository.LopTCRepository;
import com.example.sv.Repository.SinhVienRepository;
import com.example.sv.Services.LichHocServices;

@RestController
@RequestMapping("/api")
public class LichHocController {
    @Autowired
    private LopTCRepository lopTCRepository;
    @Autowired
    private LichHocRepository lichHocRepository;
    @Autowired
    private SinhVienRepository sinhVienRepository;
    @Autowired
    private LichHocServices lichHocServices;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/getschedules")
    public ResponseEntity<?> getLichHocforweek(@RequestHeader("Authorization") String authorization,
            @RequestParam String date) {
        String token = authorization.substring(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            SinhVien sinhVien = sinhVienRepository.findByEmail(email).get();
            List<LopTC> lopTCs = lopTCRepository.findBySinhViens(sinhVien);
            List<LichHoc> lichHocs = new ArrayList<>();
            for (LopTC lopTC : lopTCs) {
                List<LichHoc> lichHocsbylop = lichHocRepository.findByLopTC(lopTC);
                lichHocs.addAll(lichHocsbylop);
            }
            List<LichHoc> lichHocforWeek = lichHocServices
                    .getLichHocForWeek(localDate, lichHocs);
            lichHocforWeek.sort((a, b) -> a.getBatdau().compareTo(b.getBatdau()));
            return ResponseEntity.ok(lichHocforWeek);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }

    }

}
