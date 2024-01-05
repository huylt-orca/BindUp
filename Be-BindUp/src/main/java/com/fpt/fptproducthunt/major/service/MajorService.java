package com.fpt.fptproducthunt.major.service;

import com.fpt.fptproducthunt.common.entity.Major;
import com.fpt.fptproducthunt.major.exception.MajorNotFoundException;

import java.util.List;
import java.util.UUID;

public interface MajorService {
    List<Major> getAll();

    Major get(UUID id) throws MajorNotFoundException;

    Major createMajor(Major major);

    Major updateMajor(UUID id, Major major) throws MajorNotFoundException;

    void deleteMajor(UUID id) throws MajorNotFoundException;
}
