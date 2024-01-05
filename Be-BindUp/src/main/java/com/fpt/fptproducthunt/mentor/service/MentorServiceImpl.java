package com.fpt.fptproducthunt.mentor.service;

import com.fpt.fptproducthunt.common.entity.Mentor;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.mentor.exception.MentorExistedException;
import com.fpt.fptproducthunt.mentor.exception.MentorNotFoundException;
import com.fpt.fptproducthunt.mentor.repository.MentorRepository;
import com.fpt.fptproducthunt.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MentorServiceImpl implements MentorService {
    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ProjectService projectService;

    @Override
    public Mentor findByName(String name) {
        return mentorRepository.findByName(name);
    }

    @Override
    public Mentor findById(UUID id) throws MentorNotFoundException {
        Optional<Mentor> fetchResult = mentorRepository.findById(id);
        if (fetchResult.isPresent()) {
            return fetchResult.get();
        } else {
            throw new MentorNotFoundException("Cannot find mentor with " + id);
        }

    }

    @Override
    @Cacheable(value = "MentorList", key = "#root.methodName")
    public List<Mentor> findAll() {
        return mentorRepository.findAll();
    }

    @Override
    public List<Mentor> findAllMentorByProjectId(UUID projectId) {
        Project fetchResult = projectService.get(projectId);
        if(fetchResult != null) {
            return fetchResult.getMentors();
        }
        else {
            return null;
        }
    }

    @Override
    @CacheEvict(value = "MentorList", allEntries = true)
    public Mentor addMentor(Mentor mentor) throws MentorExistedException {
        if (mentorRepository.findByPhone(mentor.getPhone()).isPresent()) {
            throw new MentorExistedException("Mentor with phone number " + mentor.getPhone() + " already existed");
        }
        if (mentorRepository.findByEmail(mentor.getEmail()).isPresent()) {
            throw new MentorExistedException("Mentor with email " + mentor.getEmail() + " already existed");
        }
        return mentorRepository.save(mentor);
    }

    @Override
    @CacheEvict(value = "MentorList", allEntries = true)
    public Mentor updateMentor(UUID id, Mentor mentor) throws MentorNotFoundException {
        Optional<Mentor> fetchResult = mentorRepository.findById(id);
        if (fetchResult.isPresent()) {
            Mentor fetchedMentor = fetchResult.get();
            Optional<Mentor> fetchedMentorWithSamePhoneNumber = mentorRepository.findByPhone(mentor.getPhone());
            Optional<Mentor> fetchedMentorWithSameEmail = mentorRepository.findByEmail(mentor.getEmail());

            if (fetchedMentorWithSamePhoneNumber.isPresent()) {
                Mentor mentorWithSamePhoneNumber = fetchedMentorWithSamePhoneNumber.get();
                if(mentorWithSamePhoneNumber.getId() != fetchedMentor.getId()) {
                    throw new MentorExistedException("Mentor with phone number " + mentor.getPhone() + " already existed");
                }
            }
            if (fetchedMentorWithSameEmail.isPresent()) {
                Mentor mentorWithSameEmail = fetchedMentorWithSamePhoneNumber.get();
                if(mentorWithSameEmail.getId() != fetchedMentor.getId()) {
                    throw new MentorExistedException("Mentor with email " + mentor.getEmail() + " already existed");
                }
            }

            fetchedMentor.setName(mentor.getName());
            fetchedMentor.setMajor(mentor.getMajor());
            fetchedMentor.setPhone(mentor.getPhone());
            fetchedMentor.setEmail(mentor.getEmail());

            return mentorRepository.save(fetchedMentor);
        }
        else {
            throw new MentorNotFoundException("Cannot find mentor with " + id);
        }
    }

    @Override
    @CacheEvict(value = "MentorList", allEntries = true)
    public void deleteMentor(UUID id) throws MentorNotFoundException {
        Optional<Mentor> fetchResult = mentorRepository.findById(id);
        if (fetchResult.isPresent()) {
            mentorRepository.delete(fetchResult.get());
        } else {
            throw new MentorNotFoundException("Cannot find mentor with " + id);
        }
    }
}
