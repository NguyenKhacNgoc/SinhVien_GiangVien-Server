package com.example.sv.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.UserCoVan;

public interface UserCoVanRespository extends JpaRepository<UserCoVan, Long> {
    Optional<UserCoVan> findByEmail(String email);

}
