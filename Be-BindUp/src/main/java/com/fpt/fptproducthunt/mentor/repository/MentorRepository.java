package com.fpt.fptproducthunt.mentor.repository;

import com.fpt.fptproducthunt.common.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {
//    Mentor findById(UUID id);
    Mentor findByName(String name);
    Optional<Mentor> findByPhone(String phone);
    Optional<Mentor> findByEmail(String email);
}
