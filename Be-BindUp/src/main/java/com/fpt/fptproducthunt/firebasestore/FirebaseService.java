package com.fpt.fptproducthunt.firebasestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;

@Service
@Log4j2
public class FirebaseService {
	private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/fptproducthunt.appspot.com/o/%s?alt=media";
	
//    private final FirebaseApp firebaseApp;
//
//    @Autowired
//    public FirebaseService(FirebaseApp firebaseApp) {
//        this.firebaseApp = firebaseApp;
//    }

    public String uploadImage(MultipartFile imageFile) throws IOException, FirebaseAuthException {
        log.info("Generating file name");
    	String filename = UUID.randomUUID().toString();
        log.info("Getting storage client instance");
        StorageClient storageClient = StorageClient.getInstance();
        log.info("Creating/Storing image in storage bucket");
        Blob imageBlob = storageClient.bucket().create(filename, imageFile.getBytes(), "image/jpeg");
        log.info("Returning image URL");
    	return String.format(DOWNLOAD_URL, filename);
    }
}
