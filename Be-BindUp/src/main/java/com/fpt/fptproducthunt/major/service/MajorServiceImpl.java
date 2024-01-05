package com.fpt.fptproducthunt.major.service;

import com.fpt.fptproducthunt.common.entity.Major;
import com.fpt.fptproducthunt.major.exception.MajorNotFoundException;
import com.fpt.fptproducthunt.major.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorRepository majorRepository;

    @Override
    @Cacheable(value = "MajorList", key = "#root.methodName")
    public List<Major> getAll() {
        return majorRepository.findAll();
    }

    @Override
    public Major get(UUID id) throws MajorNotFoundException {
        Optional<Major> fetchResult = majorRepository.findById(id);
        if(fetchResult.isPresent()) {
            return fetchResult.get();
        }
        else {
            throw new MajorNotFoundException("Cannot find major with " + id);
        }
    }

    @Override
    @CacheEvict(value = "MajorList", allEntries = true)
    public Major createMajor(Major major) {
        return majorRepository.save(major);
    }

    @Override
    @CacheEvict(value = "MajorList", allEntries = true)
    public Major updateMajor(UUID id, Major major) throws MajorNotFoundException {
        Optional<Major> fetchResult = majorRepository.findById(id);
        if(fetchResult.isPresent()) {
            Major fetchedMajor = fetchResult.get();
            fetchedMajor.setName(major.getName());
            fetchedMajor.setDescription(major.getDescription());
            return majorRepository.save(fetchedMajor);
        }
        else {
            throw new MajorNotFoundException("Cannot find major with " + id);
        }
    }

    @Override
    @CacheEvict(value = "MajorList", allEntries = true)
    public void deleteMajor(UUID id) throws MajorNotFoundException {
        Optional<Major> fetchResult = majorRepository.findById(id);
        if(fetchResult.isPresent()) {
            majorRepository.delete(fetchResult.get());
        }
        else {
            throw new MajorNotFoundException("Cannot find major with " + id);
        }
    }
}
