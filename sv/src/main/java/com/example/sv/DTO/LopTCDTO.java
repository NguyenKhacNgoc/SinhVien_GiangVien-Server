package com.example.sv.DTO;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class LopTCDTO {

    private Long id;

    private Long mon;

    private DayOfWeek ngayhoc;

    private String buoihoc;

    private String phonghoc;

    private LocalDateTime ngaymodangky;

    private LocalDateTime ngayketthucdangky;

    private LocalDateTime ngaybatdauhoc;

    private Integer soluong;
    private String trangthai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMon() {
        return this.mon;
    }

    public void setMon(Long mon) {
        this.mon = mon;
    }

    public DayOfWeek getNgayhoc() {
        return this.ngayhoc;
    }

    public void setNgayhoc(DayOfWeek ngayhoc) {
        this.ngayhoc = ngayhoc;
    }

    public String getBuoihoc() {
        return this.buoihoc;
    }

    public void setBuoihoc(String buoihoc) {
        this.buoihoc = buoihoc;
    }

    public String getPhonghoc() {
        return this.phonghoc;
    }

    public void setPhonghoc(String phonghoc) {
        this.phonghoc = phonghoc;
    }

    public LocalDateTime getNgaymodangky() {
        return this.ngaymodangky;
    }

    public void setNgaymodangky(LocalDateTime ngaymodangky) {
        this.ngaymodangky = ngaymodangky;
    }

    public LocalDateTime getNgayketthucdangky() {
        return this.ngayketthucdangky;
    }

    public void setNgayketthucdangky(LocalDateTime ngayketthucdangky) {
        this.ngayketthucdangky = ngayketthucdangky;
    }

    public LocalDateTime getNgaybatdauhoc() {
        return this.ngaybatdauhoc;
    }

    public void setNgaybatdauhoc(LocalDateTime ngaybatdauhoc) {
        this.ngaybatdauhoc = ngaybatdauhoc;
    }

    public Integer getSoluong() {
        return this.soluong;
    }

    public void setSoluong(Integer soluong) {
        this.soluong = soluong;
    }

    public String getTrangthai() {
        return this.trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

}
