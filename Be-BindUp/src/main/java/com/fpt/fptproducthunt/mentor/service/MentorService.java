package com.fpt.fptproducthunt.mentor.service;

import com.fpt.fptproducthunt.common.entity.Mentor;
import com.fpt.fptproducthunt.mentor.exception.MentorNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.UUID;

@Service
public interface MentorService {
    Mentor findByName(String name);
    Mentor findById(UUID id) throws MentorNotFoundException;
    List<Mentor> findAll();
    List<Mentor> findAllMentorByProjectId(UUID projectId);
    Mentor addMentor(Mentor mentor);
    Mentor updateMentor(UUID id, Mentor mentor) throws MentorNotFoundException;
    void deleteMentor(UUID id) throws MentorNotFoundException;
}
