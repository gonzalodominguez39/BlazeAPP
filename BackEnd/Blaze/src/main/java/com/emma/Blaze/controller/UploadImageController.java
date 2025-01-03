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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadImageController {

    @Autowired
    IUploadFilesService uploadFilesService;

    @PostMapping("/picture")
    public ResponseEntity<Map<String, String>> uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            String resultPath = uploadFilesService.handleFileUpload(file);
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", resultPath);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al subir la imagen: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
