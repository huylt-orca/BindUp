package com.fpt.fptproducthunt.user.service;

import com.fpt.fptproducthunt.account.repository.AccountRepository;
import com.fpt.fptproducthunt.common.entity.Account;
import com.fpt.fptproducthunt.common.entity.User;
import com.fpt.fptproducthunt.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        Optional<User> optionalUser = account.isPresent() ? Optional.of(account.get().getUser()) : Optional.empty();
        return optionalUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserID(UUID id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User uploadAvatar(UUID userId, String avatar) {
        User user = userRepository.findById(userId).get();

        user.setAvatar(avatar);

        user = userRepository.save(user);
        return user;
    }
}
