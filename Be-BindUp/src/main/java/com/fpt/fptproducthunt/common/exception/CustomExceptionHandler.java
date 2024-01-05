package com.fpt.fptproducthunt.common.exception;

import com.fpt.fptproducthunt.common.dto.ErrorMessage;
import com.fpt.fptproducthunt.major.exception.MajorNotFoundException;
import com.fpt.fptproducthunt.mentor.exception.MentorExistedException;
import com.fpt.fptproducthunt.mentor.exception.MentorNotFoundException;
import com.fpt.fptproducthunt.notification.exception.NotificationNotFoundException;
import com.fpt.fptproducthunt.project.exception.ProjectNotFoundException;
import com.fpt.fptproducthunt.topic.exception.TopicExistedException;
import com.fpt.fptproducthunt.topic.exception.TopicNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@CrossOrigin
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorMessage> projectNotFoundException(ProjectNotFoundException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorMessage> notificationNotFoundException(NotificationNotFoundException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<ErrorMessage> topicNotFoundException(TopicNotFoundException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TopicExistedException.class)
    public ResponseEntity<ErrorMessage> topicExistedException(TopicExistedException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MentorNotFoundException.class)
    public ResponseEntity<ErrorMessage> mentorNotFoundException(MentorNotFoundException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MentorExistedException.class)
    public ResponseEntity<ErrorMessage> mentorExistedException(MentorExistedException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MajorNotFoundException.class)
    public ResponseEntity<ErrorMessage> majorNotFoundException(MajorNotFoundException exception) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> commonExceptionHandling(Throwable throwable) {
        return new ResponseEntity<>(new ErrorMessage(new Date(), throwable.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
