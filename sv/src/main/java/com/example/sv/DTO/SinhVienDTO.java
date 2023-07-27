package com.example.sv.DTO;

import java.util.Date;

import com.example.sv.Entity.LopHC;
import com.fasterxml.jackson.annotation.JsonFormat;

public class SinhVienDTO {
    private Long id;
    private String masv;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ngaysinh;
    private String email;
    private LopHC lopHC;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMasv() {
        return this.masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNgaysinh() {
        return this.ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LopHC getLopHC() {
        return this.lopHC;
    }

    public void setLopHC(LopHC lopHC) {
        this.lopHC = lopHC;
    }

}
