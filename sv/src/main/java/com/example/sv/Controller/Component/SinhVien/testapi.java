package com.example.sv.Controller.Component.SinhVien;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sv.Entity.LichHoc;
import com.example.sv.Entity.LopTC;
import com.example.sv.Repository.LichHocRepository;
import com.example.sv.Repository.LopTCRepository;
import com.example.sv.Services.LichHocServices;

@RestController

public class testapi {
    @Autowired
    private LopTCRepository lopTCRepository;
    @Autowired
    private LichHocServices lichHocServices;
    @Autowired
    private LichHocRepository lichHocRepository;

    @GetMapping("/testlich")
    public void testlich(@RequestParam Long id) {
        Optional<LopTC> existingLopTC = lopTCRepository.findById(id);
        LopTC lopTC = existingLopTC.get();
        lichHocServices.generateLichHoc(lopTC);

    }

    @GetMapping("/xemlich")
    public List<LichHoc> xemlich(@RequestParam Long loptc) {
        return lichHocRepository.findByLopTC(lopTCRepository.findById(loptc).get());
    }

}
