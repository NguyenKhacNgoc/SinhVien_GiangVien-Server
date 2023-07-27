package com.example.sv.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.GiangVien;
import com.example.sv.Entity.LopHC;

public interface LopHCRepository extends JpaRepository<LopHC, Long> {
    List<LopHC> findByCovan(GiangVien covan);

}
