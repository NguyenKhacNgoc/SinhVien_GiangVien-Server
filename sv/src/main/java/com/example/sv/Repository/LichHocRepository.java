package com.example.sv.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.LichHoc;
import com.example.sv.Entity.LopTC;

import java.util.List;

public interface LichHocRepository extends JpaRepository<LichHoc, Long> {
    List<LichHoc> findByLopTC(LopTC lopTC);

}
