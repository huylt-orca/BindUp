package com.fpt.fptproducthunt.major.repository;

import com.fpt.fptproducthunt.common.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {
}
