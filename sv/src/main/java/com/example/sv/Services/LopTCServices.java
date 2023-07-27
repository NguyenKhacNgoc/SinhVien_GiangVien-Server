package com.example.sv.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sv.Entity.LopTC;

@Service
public class LopTCServices {

    public List<LopTC> GetLopTCByNgayMoDKAndNgayKT(List<LopTC> lopTCs) {
        // Lấy ngày hiện tại
        LocalDateTime currentDate = LocalDateTime.now();
        // Cho xem lịch đăng ký tín trước 3 ngày và có hạn bổ sung đăng ký là 1 tuần
        LocalDateTime startDate = currentDate.plusDays(3);
        LocalDateTime endDate = currentDate.minusWeeks(1);
        List<LopTC> lopTCsOK = lopTCs.stream()
                .filter(ltc -> ltc.getNgaymodangky().isBefore(startDate)
                        && ltc.getNgayketthucdangky().isAfter(endDate))
                .collect(Collectors.toList());
        return lopTCsOK;
    }

}
