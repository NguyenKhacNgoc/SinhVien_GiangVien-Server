package com.example.sv.Controller.Component.Admin;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.DTO.LopTCDTO;
import com.example.sv.Entity.LopTC;
import com.example.sv.Entity.Mon;
import com.example.sv.Repository.LopTCRepository;
import com.example.sv.Repository.MonRepository;
import com.example.sv.Services.LichHocServices;

@RestController
@RequestMapping("/api/admin")
public class AdLopTC {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LopTCRepository lopTCRepository;
    @Autowired
    MonRepository monRepository;
    @Autowired
    private LichHocServices lichHocServices;

    @GetMapping("/getlopTC")
    public ResponseEntity<?> getLopTC(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            // Tạo Map chứa danh sách lớp tín chỉ và môn
            Map<String, Object> response = new HashMap<>();
            response.put("mons", monRepository.findAll());
            response.put("lopTCs", lopTCRepository.findAll());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

    @PostMapping("addclass")
    public ResponseEntity<?> addclass(@RequestHeader("Authorization") String authorization,
            @RequestBody LopTCDTO request) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            // Kiểm tra xem lớp đã tồn tại chưa
            Mon mon = monRepository.findById(request.getMon()).get();
            if (lopTCRepository.findByMonAndNgayhocAndBuoihocAndPhonghoc(mon, request.getNgayhoc(),
                    request.getBuoihoc(), request.getPhonghoc()).isPresent()
                    && lopTCRepository.findByMonAndNgayhocAndBuoihocAndPhonghoc(mon, request.getNgayhoc(),
                            request.getBuoihoc(), request.getPhonghoc()).get().getTrangthai()
                            .equals("Chưa hoàn thành")) {
                return ResponseEntity.badRequest().body("Lớp này đang dược mở");
            } else {
                LopTC lopTC = new LopTC();
                lopTC.setMon(mon);
                lopTC.setNgayhoc(request.getNgayhoc());
                lopTC.setBuoihoc(request.getBuoihoc());
                lopTC.setPhonghoc(request.getPhonghoc());
                lopTC.setSoluong(request.getSoluong());
                lopTC.setNgaymodangky(request.getNgaymodangky());
                lopTC.setNgayketthucdangky(request.getNgayketthucdangky());
                lopTC.setNgaybatdauhoc(request.getNgaybatdauhoc());
                lopTC.setTrangthai("Chưa hoàn thành");
                lopTCRepository.save(lopTC);
                lichHocServices.generateLichHoc(lopTC);
                return ResponseEntity.ok("Mở lớp thành công");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

    @DeleteMapping("/deleteclass")
    public ResponseEntity<?> deleteclass(@RequestHeader("Authorization") String authorization,
            @RequestParam Long loptc) {
        String token = authorization.substring(7);
        if (jwtTokenUtil.validateToken(token)) {
            Optional<LopTC> existinglop = lopTCRepository.findById(loptc);
            if (existinglop.isPresent()) {
                lichHocServices.DeleteLichHoc(existinglop.get());
                lopTCRepository.delete(existinglop.get());
                // Xoá luôn lịch học

                return ResponseEntity.ok("Huỷ thành công lớp này");
            } else {
                return ResponseEntity.badRequest().body("Lớp này không còn tồn tại");

            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }
    }

}
