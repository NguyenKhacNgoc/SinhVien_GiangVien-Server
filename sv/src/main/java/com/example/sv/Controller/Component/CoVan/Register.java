package com.example.sv.Controller.Component.CoVan;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.DTO.UserDTO;
import com.example.sv.Entity.GiangVien;
import com.example.sv.Entity.LopHC;
import com.example.sv.Entity.TempUser;
import com.example.sv.Entity.User;
import com.example.sv.Repository.GiangVienRespository;
import com.example.sv.Repository.LopHCRepository;
import com.example.sv.Repository.TempUserRepository;
import com.example.sv.Repository.UserRepository;
import com.example.sv.Services.EmailServices;

@RestController
@RequestMapping("/api/covan")
public class Register {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TempUserRepository tempUserRepository;
    @Autowired
    private EmailServices emailServices;
    @Autowired
    private LopHCRepository lopHCRepository;
    @Autowired
    GiangVienRespository giangVienRespository;

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody UserDTO request) {
        Optional<GiangVien> gv = giangVienRespository.findByEmail(request.getEmail());
        // Optional<LopHC> existingCV = lopHCRepository.findByCovan(gv.get());
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        Optional<TempUser> existingTempUser = tempUserRepository.findByEmail(request.getEmail());
        if (gv.isPresent()) {
            List<LopHC> lopHCs = lopHCRepository.findByCovan(gv.get());
            if (lopHCs != null) {
                if (existingUser.isPresent()) {
                    return ResponseEntity.badRequest().body("Tài khoản đã tồn tại");
                } else {
                    if (existingTempUser.isPresent()) {
                        Random random = new Random();
                        int randomCode = random.nextInt(999999 - 100000 + 1) + 100000;
                        // Thực hiện gửi lại mã và thay bằng mã mới, set lại thời gian hết hạn
                        emailServices.sendVerificationCode(request.getEmail(), randomCode);
                        TempUser tempUser = existingTempUser.get();
                        tempUser.setVerificationcode(randomCode);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MINUTE, 5);
                        tempUser.setExpiredtime(calendar.getTime());
                        tempUserRepository.save(tempUser);

                        return ResponseEntity.ok("Đã gửi lại mã");
                    } else {

                        // Gửi mã xác minh ở đây
                        Random random = new Random();
                        int randomCode = random.nextInt(999999 - 100000 + 1) + 100000;
                        emailServices.sendVerificationCode(request.getEmail(), randomCode);
                        // Lưu vào TempUser
                        TempUser tempUser = new TempUser();
                        tempUser.setEmail(request.getEmail());
                        tempUser.setVerificationcode(randomCode);
                        tempUser.setVerificated(false);
                        // Thiết lập thời gian hết hạn
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.MINUTE, 5);
                        tempUser.setExpiredtime(calendar.getTime());
                        tempUserRepository.save(tempUser);

                        return ResponseEntity.ok("Truy cập email để lấy mã xác minh");
                    }
                }

            } else {
                return ResponseEntity.badRequest().body("Bạn không phải cố vấn học tập");
            }
        } else {
            return ResponseEntity.badRequest().body("Bạn không phải giảng viên");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO request) {
        Optional<TempUser> existingTempUser = tempUserRepository.findByEmail(request.getEmail());
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Tài khoản đã tồn tại");
        } else {
            if (existingTempUser.isPresent()) {
                if (existingTempUser.get().getVerificationcode() == request.getVerificationCode()) {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());

                    userRepository.save(user);
                    // Xoá TempUser sau khi lưu người dùng vào User
                    tempUserRepository.delete(existingTempUser.get());
                    return ResponseEntity.ok("Đăng ký thành công, vui lòng đăng nhập vào hệ thống");

                } else {
                    return ResponseEntity.badRequest()
                            .body("Mã xác minh không đúng. Nếu chưa nhận được mã hãy yêu cầu chúng tôi gửi lại");
                }
            } else {
                return ResponseEntity.badRequest().body("Tài khoản này không tồn tại");
            }
        }

    }

}
