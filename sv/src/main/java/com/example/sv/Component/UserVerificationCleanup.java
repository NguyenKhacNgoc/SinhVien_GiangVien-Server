package com.example.sv.Component;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.sv.Repository.TempUserRepository;

@Component
public class UserVerificationCleanup {
    @Autowired
    private TempUserRepository tempUserRepository;

    @Scheduled(fixedDelay = 60000)
    public void cleanupUnverifiedTempUsers() {
        Date currentTime = new Date();
        tempUserRepository.deleteByVerificatedFalseAndExpiredtimeLessThanEqual(currentTime);

    }

}
