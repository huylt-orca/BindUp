package com.fpt.fptproducthunt.firebasestore.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.fptproducthunt.firebasestore.FirebaseService;
import com.google.firebase.auth.FirebaseAuthException;

@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/api/v1/images")
@Tag(name = "Image APIs")
public class ImageController {
	
	@Autowired
    private FirebaseService firebaseService;
    @PostMapping(value="/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestPart(required = true) MultipartFile imageFile) throws FirebaseAuthException {
        try {
            log.info("Calling firebase service");
            String fileName = firebaseService.uploadImage(imageFile);
            log.info("Returning response with image URL");
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            log.info(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
