package com.example.sv.Services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.temporal.TemporalAdjusters;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sv.Entity.LichHoc;
import com.example.sv.Entity.LopTC;
import com.example.sv.Repository.LichHocRepository;

@Service
public class LichHocServices {
    @Autowired
    private LichHocRepository lichHocRepository;

    public void generateLichHoc(LopTC lopTC) {
        // Trường hợp thêm mới lớp tín chỉ
        // Lấy thông tin lịch học từ lớp tín chỉ
        DayOfWeek ngayHoc = lopTC.getNgayhoc();
        String buoiHoc = lopTC.getBuoihoc();
        LocalDate ngayhoctu = lopTC.getNgaybatdauhoc().toLocalDate();

        // Xử lý ngày học và buổi học để tính toán thời gian bắt đầu và kết thúc
        // Trong ví dụ này, giả sử thời gian bắt đầu là 7:00 và kết thúc là 11:30 cho
        // buổi sáng,
        // và 13:00 đến 17:30 cho buổi chiều.
        LocalTime startTime;
        LocalTime endTime;

        if (buoiHoc.equalsIgnoreCase("Sáng")) {
            startTime = LocalTime.of(7, 0);
            endTime = LocalTime.of(11, 30);
        } else {
            startTime = LocalTime.of(13, 0);
            endTime = LocalTime.of(17, 30);
        }
        // Chuyển Date sang LocalDate
        // LocalDate ngayhoctu =
        // handangky.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // Tạo lịch học cho 12 tuần liên tiếp
        for (int i = 0; i < 12; i++) {
            // Tìm ngày gần nhất của tuần mà là ngày học
            LocalDate nextHoc = ngayhoctu.with(TemporalAdjusters.nextOrSame(ngayHoc));
            // Tính toán thời gian bắt đầu và kết thúc dựa trên ngày học và buổi học
            LocalDateTime startDateTime = LocalDateTime.of(nextHoc, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(nextHoc, endTime);
            // Lưu thông tin lịch học vào bảng LichHoc
            LichHoc lichHoc = new LichHoc();
            lichHoc.setLopTC(lopTC);
            lichHoc.setBatdau(startDateTime);
            lichHoc.setKetthuc(endDateTime);
            lichHoc.setTrangthai("Học");
            lichHocRepository.save(lichHoc);
            // Tăng ngày bắt đầu học lên 1 tuần để tạo lịch học cho tuần tiếp theo
            ngayhoctu = ngayhoctu.plusWeeks(1);

        }

    }

    public void DeleteLichHoc(LopTC lopTC) {
        List<LichHoc> lichHocs = lichHocRepository.findByLopTC(lopTC);
        lichHocRepository.deleteAll(lichHocs);

    }

    public List<LichHoc> getLichHocForWeek(LocalDate date, List<LichHoc> lichHocs) {
        // Tính ngày đầu và cuối tuần
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // Danh sách lịch học của sinh viên
        // Lọc theo tuần
        List<LichHoc> lichHocForWeek = lichHocs.stream()
                .filter(lh -> lh.getBatdau().toLocalDate().isEqual(startDate)
                        || lh.getBatdau().toLocalDate().isAfter(startDate))
                .filter(lh -> lh.getKetthuc().toLocalDate().isEqual(endDate)
                        || lh.getKetthuc().toLocalDate().isBefore(endDate))
                .collect(Collectors.toList());
        return lichHocForWeek;

    }

}
