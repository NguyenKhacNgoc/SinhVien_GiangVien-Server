package com.example.sv.Repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.sv.Entity.TempUser;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
    Optional<TempUser> findByEmail(String email);

    @Transactional
    void deleteByVerificatedFalseAndExpiredtimeLessThanEqual(Date currentTime);

}
