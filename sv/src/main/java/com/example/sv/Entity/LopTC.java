package com.example.sv.Entity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopTC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "mon_id")
    private Mon mon;
    @Column
    @Enumerated(EnumType.STRING)
    private DayOfWeek ngayhoc;
    @Column
    private String buoihoc;
    @Column
    private String phonghoc;
    @Column
    private LocalDateTime ngaymodangky;
    @Column
    private LocalDateTime ngayketthucdangky;
    @Column
    private LocalDateTime ngaybatdauhoc;
    @Column
    private Integer soluong;
    @Column
    private String trangthai;
    @ManyToMany
    @JoinTable(name = "dangki_tinchi", joinColumns = @JoinColumn(name = "loptc_id"), inverseJoinColumns = @JoinColumn(name = "sinhvien_id"))
    private List<SinhVien> sinhViens;

}
