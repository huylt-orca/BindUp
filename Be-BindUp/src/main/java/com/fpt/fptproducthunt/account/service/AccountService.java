package com.fpt.fptproducthunt.account.service;

import com.fpt.fptproducthunt.common.entity.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface AccountService{
    Optional<Account> findByUsername(String username);

    void updateDeviceToken(UUID userId, String deviceToken);
}
