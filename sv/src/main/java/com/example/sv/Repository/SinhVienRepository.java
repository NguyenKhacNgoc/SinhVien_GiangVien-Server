package com.example.sv.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.LopHC;
import com.example.sv.Entity.SinhVien;

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    List<SinhVien> findByLopHC(LopHC lopHC);

    Optional<SinhVien> findByEmail(String email);

    List<SinhVien> findByMasv(String masv);

}
