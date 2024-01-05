package com.fpt.fptproducthunt.topic.service;

import com.fpt.fptproducthunt.common.entity.Topic;
import com.fpt.fptproducthunt.topic.exception.TopicNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TopicService {
    List<Topic> getAll();

    Topic get(UUID id) throws TopicNotFoundException;

    Topic createTopic(Topic topic);

    Topic updateTopic(UUID id, Topic topic) throws TopicNotFoundException;

    void deleteTopic(UUID id) throws TopicNotFoundException;
}
