package com.example.sv.Controller.Component.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.DTO.DiemDTO;
import com.example.sv.Entity.Diem;
import com.example.sv.Entity.LopTC;
import com.example.sv.Entity.SinhVien;
import com.example.sv.Repository.DiemRepository;
import com.example.sv.Repository.LopTCRepository;
import com.example.sv.Repository.SinhVienRepository;

@RestController
@RequestMapping("/api/admin")
public class AdDiemSV {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SinhVienRepository sinhVienRepository;
    @Autowired
    private DiemRepository diemRepository;
    @Autowired
    private LopTCRepository lopTCRepository;

    @GetMapping("/getdiemsv")
    public ResponseEntity<?> getdiemsv(@RequestHeader("Authorization") String authorization, @RequestParam Long loptcID,
            @RequestParam Long sinhvienID) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            Optional<SinhVien> existingSV = sinhVienRepository.findById(sinhvienID);
            Optional<LopTC> existinglopTC = lopTCRepository.findById(loptcID);
            if (existingSV.isPresent() && existinglopTC.isPresent()) {
                List<LopTC> lopTCs = lopTCRepository.findBySinhViens(existingSV.get());
                if (lopTCs.contains(existinglopTC.get())) {
                    Diem diem = diemRepository.findBySinhVienAndLopTC(existingSV.get(), existinglopTC.get()).get();
                    return ResponseEntity.ok(diem);
                } else
                    return ResponseEntity.badRequest().body("Lớp tín chỉ không tồn tại");

            } else {
                return ResponseEntity.badRequest().body("Sinh viên không tồn tại");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }

    }

    @PutMapping("/putdiem")
    public ResponseEntity<?> putdiem(@RequestHeader("Authorization") String authorization,
            @RequestBody DiemDTO request) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            Optional<SinhVien> existingSV = sinhVienRepository.findById(request.getSinhVien());
            Optional<LopTC> existinglopTC = lopTCRepository.findById(request.getLopTC());
            if (existingSV.isPresent() && existinglopTC.isPresent()) {
                List<LopTC> lopTCs = lopTCRepository.findBySinhViens(existingSV.get());
                if (lopTCs.contains(existinglopTC.get())) {
                    Diem diem = diemRepository.findBySinhVienAndLopTC(existingSV.get(), existinglopTC.get()).get();
                    diem.setCc(request.getCc());
                    diem.setGk(request.getGk());
                    diem.setThi(request.getThi());
                    diemRepository.save(diem);
                    LopTC lopTC = existinglopTC.get();
                    if (diem.getCc() != null && diem.getGk() != null && diem.getThi() != null) {

                        lopTC.setTrangthai("Đã xong");
                        lopTCRepository.save(lopTC);

                    } else {

                        lopTC.setTrangthai("Chưa hoàn thành");
                        lopTCRepository.save(lopTC);

                    }
                    return ResponseEntity.ok("Thành công");
                } else
                    return ResponseEntity.badRequest().body("Lớp tín chỉ không tồn tại");

            } else {
                return ResponseEntity.badRequest().body("Sinh viên không tồn tại");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

}
