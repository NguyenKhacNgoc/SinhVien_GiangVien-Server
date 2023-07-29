package com.example.sv.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sv.Entity.UserAdmin;

public interface UserAdminRespository extends JpaRepository<UserAdmin, Long> {
    Optional<UserAdmin> findByEmail(String email);

}
