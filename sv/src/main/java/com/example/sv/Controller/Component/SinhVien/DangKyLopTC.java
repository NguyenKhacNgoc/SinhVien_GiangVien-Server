package com.example.sv.Controller.Component.SinhVien;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Component.JwtTokenUtil;
import com.example.sv.DTO.CoVanDKTCDTO;
import com.example.sv.Entity.Diem;
import com.example.sv.Entity.LopTC;
import com.example.sv.Entity.Mon;
import com.example.sv.Entity.SinhVien;
import com.example.sv.Repository.DiemRepository;
import com.example.sv.Repository.LopTCRepository;
import com.example.sv.Repository.MonRepository;
import com.example.sv.Repository.SinhVienRepository;
import com.example.sv.Services.LopTCServices;

@RestController
@RequestMapping("/api")
public class DangKyLopTC {

    @Autowired
    private SinhVienRepository sinhVienRepository;
    @Autowired
    private LopTCRepository lopTCRepository;
    @Autowired
    private MonRepository monRepository;
    @Autowired
    private DiemRepository diemRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LopTCServices lopTCServices;

    @GetMapping("/lich")
    public ResponseEntity<?> getLopTC(@RequestHeader("Authorization") String authorization) {
        try {

            String token = authorization.substring(7);// Bỏ qua Bearer để lấy token
            // Xác thực token
            if (jwtTokenUtil.validateToken(token)) {
                /*
                 * Chỉ hiện ra môn mà chưa đăng ký
                 * không trùng môn
                 * không trùng lịch học(ngày học và buổi học)
                 */
                String email = jwtTokenUtil.getEmailFromToken(token);
                SinhVien sinhVien = sinhVienRepository.findByEmail(email).get();
                // Danh sách lớp tín chỉ mà sinh viên này đã đăng ký
                List<LopTC> lopTCdadk = lopTCRepository.findBySinhViens(sinhVien);
                // Lấy ra các môn mà sinh viên đã đăng ký

                List<Mon> mondadk = new ArrayList<>();
                for (LopTC lopTC : lopTCdadk) {
                    mondadk.add(lopTC.getMon());
                }

                // Lấy ra tất cả các môn
                List<Mon> mons = monRepository.findAll();
                // Lọc ra danh sách môn chưa đăng ký
                List<Mon> moncdk = mons.stream().filter(mon -> !mondadk.contains(mon)).collect(Collectors.toList());
                // test
                List<LopTC> lopTCcdk = new ArrayList<>();

                for (Mon mon : moncdk) {
                    List<LopTC> list = lopTCRepository.findByMon(mon);
                    lopTCcdk.addAll(list);

                }
                // Lấy ra lớp tín chỉ chưa đăng ký
                // List<LopTC> lopTCcdk = lopTCRepository.findByMonIn(moncdk);
                List<LopTC> lopTCOK = new ArrayList<>();
                for (LopTC lopTC : lopTCcdk) {
                    boolean isDuplicateNgayhocAndBuoihoc = lopTCdadk.stream()
                            .anyMatch(l -> l.getNgayhoc().equals(lopTC.getNgayhoc())
                                    && l.getBuoihoc().equals(lopTC.getBuoihoc()));
                    if (!isDuplicateNgayhocAndBuoihoc) {
                        lopTCOK.add(lopTC);
                    }
                }
                return ResponseEntity.ok(lopTCServices.GetLopTCByNgayMoDKAndNgayKT(lopTCOK));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi ngoại lệ");
        }

    }

    @PutMapping("/addclass")
    public ResponseEntity<?> addLopTC(@RequestHeader("Authorization") String authorization,
            @RequestBody CoVanDKTCDTO request) {

        String token = authorization.substring(7);// Bỏ qua Bearer để lấy token
        // Xác thực token
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            Optional<SinhVien> existingSV = sinhVienRepository.findByEmail(email);
            Optional<LopTC> existinglopTC = lopTCRepository.findById(request.getLopID());
            if (existinglopTC.isPresent()) {
                LopTC lopTC = existinglopTC.get();
                List<SinhVien> sinhViens = lopTC.getSinhViens();
                if (!sinhViens.contains(existingSV.get())) {
                    sinhViens.add(existingSV.get());
                    lopTC.setSinhViens(sinhViens);
                    lopTCRepository.save(lopTC);
                    // Thêm sinh viên và lớp tín chỉ vào bảng điểm
                    Diem diem = new Diem();
                    diem.setSinhVien(existingSV.get());
                    diem.setLopTC(lopTC);
                    diemRepository.save(diem);

                    return ResponseEntity.ok("Đăng ký thành công");

                } else {
                    return ResponseEntity.badRequest().body("Bạn đã đăng ký lớp này");
                }

            } else {
                return ResponseEntity.badRequest().body("Lớp này không tồn tại");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }

    }

    @GetMapping("/registeredclass")
    public ResponseEntity<?> getRegisteredClass(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);// Bỏ qua Bearer để lấy token
        // Xác thực token
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            SinhVien sinhVien = sinhVienRepository.findByEmail(email).get();
            // Danh sách lớp tín chỉ mà sinh viên này đã đăng ký
            List<LopTC> lopTCs = lopTCRepository.findBySinhViens(sinhVien);

            return ResponseEntity.ok(lopTCs);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }

    }

    @PutMapping("/removeclass")
    public ResponseEntity<?> removeClass(@RequestHeader("Authorization") String authorization,
            @RequestBody CoVanDKTCDTO request) {
        String token = authorization.substring(7);// Bỏ qua Bearer để lấy token
        // Xác thực token
        if (jwtTokenUtil.validateToken(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            Optional<SinhVien> existingSV = sinhVienRepository.findByEmail(email);

            Optional<LopTC> existingLopTC = lopTCRepository.findById(request.getLopID());
            if (existingLopTC.isPresent()) {
                LopTC lopTC = existingLopTC.get();
                List<SinhVien> sinhViens = lopTC.getSinhViens();
                SinhVien sinhViened = existingSV.get();
                if (sinhViens.removeIf(sv -> sv.equals(sinhViened))) {
                    lopTCRepository.save(lopTC);
                    // Xoá bảng điểm của sinh viên này
                    if (diemRepository.findBySinhVienAndLopTC(sinhViened, lopTC).isPresent()) {
                        diemRepository.delete(diemRepository.findBySinhVienAndLopTC(sinhViened, lopTC).get());
                    }
                    return ResponseEntity.ok("Đã xoá khỏi lớp");

                } else {
                    return ResponseEntity.badRequest().body("Bạn đã không còn trong lớp này");

                }

            }

            else {
                return ResponseEntity.badRequest().body("Lớp tín chỉ không tồn tại");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Xác thực thất bại");
        }

    }

}
