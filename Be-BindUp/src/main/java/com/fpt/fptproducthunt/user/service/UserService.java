package com.fpt.fptproducthunt.user.service;

import com.fpt.fptproducthunt.common.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService{
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findByUserID(UUID id);
    User update(User user);

    User uploadAvatar(UUID userId, String avatar);
}
