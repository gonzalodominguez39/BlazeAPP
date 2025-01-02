package com.emma.Blaze.service;

import com.emma.Blaze.utils.IUploadFilesService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFilesServiceImpl implements IUploadFilesService {
    private static final String UPLOAD_DIR = "src/main/resources/picture/";

    @Override
    public String handleFileUpload(MultipartFile file) throws IOException {

        long maxFileSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxFileSize) {
            return "El tamaño del archivo debe ser menor o igual a 5 MB";
        }


        String fileOriginalName = file.getOriginalFilename();
        if (fileOriginalName == null || !(fileOriginalName.endsWith(".jpg") || !fileOriginalName.endsWith(".jpeg") || !fileOriginalName.endsWith(".webp") || !fileOriginalName.endsWith(".png"))) {
            return "Solo se permiten archivos JPG, JPEG , PNG o WEBP";
        }


        String fileName = UUID.randomUUID().toString();
        String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        String newFileName = fileName + fileExtension;


        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }


        Path filePath = Paths.get(UPLOAD_DIR + newFileName);

        try {

            Files.write(filePath, file.getBytes());
            return "Archivo subido exitosamente: " + newFileName;
        } catch (IOException e) {

            throw new IOException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
}
