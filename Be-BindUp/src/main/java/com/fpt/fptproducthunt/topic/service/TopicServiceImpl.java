package com.fpt.fptproducthunt.topic.service;

import com.fpt.fptproducthunt.common.entity.Topic;
import com.fpt.fptproducthunt.topic.exception.TopicExistedException;
import com.fpt.fptproducthunt.topic.exception.TopicNotFoundException;
import com.fpt.fptproducthunt.topic.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Override
    @Cacheable(value = "TopicList", key = "#root.methodName")
    public List<Topic> getAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic get(UUID id) throws TopicNotFoundException {
        Optional<Topic> fetchResult = topicRepository.findById(id);
        if(fetchResult.isPresent()) {
            return fetchResult.get();
        }
        else {
            throw new TopicNotFoundException("Cannot find topic with" + id);
        }
    }

    @Override
    @CacheEvict(value = "TopicList", allEntries = true)
    public Topic createTopic(Topic topic) throws TopicExistedException {
        if(topicRepository.findByName(topic.getName()).isPresent()) {
            throw new TopicExistedException("Topic with name " + topic.getName() + " already existed");
        }
        if(topicRepository.findByShortName(topic.getShortName()).isPresent()) {
            throw new TopicExistedException("Topic with short name " + topic.getShortName() + " already existed");
        }
        return topicRepository.save(topic);
    }

    @Override
    @CacheEvict(value = "TopicList", allEntries = true)
    public Topic updateTopic(UUID id, Topic topic) throws TopicNotFoundException {
        Optional<Topic> fetchResult = topicRepository.findById(id);
        if(fetchResult.isPresent()) {
            Topic fetchedTopic = fetchResult.get();
            Optional<Topic> fetchedTopicWithSameName = topicRepository.findByName(topic.getName());
            Optional<Topic> fetchedTopicWithSameShortName = topicRepository.findByShortName(topic.getShortName());

            if(fetchedTopicWithSameName.isPresent()) {
                Topic topicWithSameName = fetchedTopicWithSameName.get();
                if(topicWithSameName.getId() != fetchedTopic.getId()) {
                    throw new TopicExistedException("Topic with name " + topic.getName() + " already existed");
                }
            }
            if(fetchedTopicWithSameShortName.isPresent()) {
                Topic topicWithSameShortName = fetchedTopicWithSameShortName.get();
                if(topicWithSameShortName.getId() != fetchedTopic.getId()) {
                    throw new TopicExistedException("Topic with short name " + topic.getShortName() + " already existed");
                }
            }

            fetchedTopic.setDescription(topic.getDescription());
            fetchedTopic.setName(topic.getName());
            fetchedTopic.setShortName(topic.getShortName());

            return topicRepository.save(fetchedTopic);
        }
        else {
            throw new TopicNotFoundException("Cannot find topic with" + id);
        }
    }

    @Override
    @CacheEvict(value = "TopicList", allEntries = true)
    public void deleteTopic(UUID id) throws TopicNotFoundException {
        Optional<Topic> fetchResult = topicRepository.findById(id);
        if(fetchResult.isPresent()) {
            topicRepository.delete(fetchResult.get());
        }
        else {
            throw new TopicNotFoundException("Cannot find topic with" + id);
        }
    }
}