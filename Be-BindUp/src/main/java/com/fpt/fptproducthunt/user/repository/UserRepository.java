package com.fpt.fptproducthunt.user.repository;

import com.fpt.fptproducthunt.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    User findByAccount_Username(String username);
}
