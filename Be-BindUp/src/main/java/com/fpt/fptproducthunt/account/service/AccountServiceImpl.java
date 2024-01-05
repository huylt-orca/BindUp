package com.fpt.fptproducthunt.account.service;

import com.fpt.fptproducthunt.account.repository.AccountRepository;
import com.fpt.fptproducthunt.common.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public void updateDeviceToken(UUID userId, String deviceToken) {
        Account account = accountRepository.findById(userId).get();

        account.setDeviceToken(deviceToken);

        accountRepository.save(account);
    }
}
