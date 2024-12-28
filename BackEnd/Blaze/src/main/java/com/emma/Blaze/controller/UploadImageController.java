package com.emma.Blaze.controller;

import com.emma.Blaze.utils.IUploadFilesService;
import jakarta.mail.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadImageController {

    @Autowired
    IUploadFilesService uploadFilesService;

    @PostMapping("/picture")
    public ResponseEntity<String> uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            String resultMessage = uploadFilesService.handleFileUpload(file);
            return new ResponseEntity<>(resultMessage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al subir la imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
