package com.example.sv.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.Diem;
import com.example.sv.Entity.SinhVien;
import com.example.sv.Entity.LopTC;

public interface DiemRepository extends JpaRepository<Diem, Long> {
    List<Diem> findBySinhVien(SinhVien sinhVien);

    Optional<Diem> findBySinhVienAndLopTC(SinhVien sinhVien, LopTC lopTC);

}
