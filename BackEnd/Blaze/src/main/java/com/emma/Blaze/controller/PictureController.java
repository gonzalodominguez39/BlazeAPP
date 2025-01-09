package com.emma.Blaze.controller;

import com.emma.Blaze.utils.IUploadFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    @Autowired
    IUploadFilesService uploadFilesService;

    @PostMapping("/upload")
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

    @GetMapping("/photo/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        Path photoPath = Paths.get("/home/emma/pictures/").resolve(filename);

        if (Files.exists(photoPath)) {
            Resource resource = new FileSystemResource(photoPath);

            String contentType = null;
            try {
                contentType = Files.probeContentType(photoPath);
                if (contentType == null) {

                    contentType = "application/octet-stream";
                }
            } catch (IOException e) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


