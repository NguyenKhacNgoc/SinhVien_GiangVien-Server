package com.example.sv.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.LopTC;
import com.example.sv.Entity.Mon;
import com.example.sv.Entity.SinhVien;

public interface LopTCRepository extends JpaRepository<LopTC, Long> {
    List<LopTC> findBySinhViens(SinhVien sinhViens);

    List<LopTC> findByMon(Mon mon);

    Optional<LopTC> findByMonAndNgayhocAndBuoihocAndPhonghoc(Mon mon, DayOfWeek ngayhoc, String buoihoc,
            String phonghoc);

}
