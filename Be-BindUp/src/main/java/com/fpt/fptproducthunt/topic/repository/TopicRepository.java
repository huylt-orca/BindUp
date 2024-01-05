package com.fpt.fptproducthunt.topic.repository;

import com.fpt.fptproducthunt.common.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    Optional<Topic> findByName(String name);
    Optional<Topic> findByShortName(String name);
}
