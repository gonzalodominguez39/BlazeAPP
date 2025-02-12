package com.emma.Blaze.service;

import com.emma.Blaze.utils.IUploadFilesService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UploadFilesServiceImpl implements IUploadFilesService {
    private static final String UPLOAD_DIR = "/home/emma/pictures/";

    @Override
    public String handleFileUpload(MultipartFile file) throws IOException {
        long maxFileSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxFileSize) {
            return "El tamaño del archivo debe ser menor o igual a 5 MB";
        }

        String fileOriginalName = file.getOriginalFilename();
        String contentType = file.getContentType();

    
        if (contentType == null || !contentType.startsWith("image/")) {
            return "Tipo de contenido no válido. Se esperaba una imagen.";
        }


        String fileExtension = "";
        if (fileOriginalName != null && fileOriginalName.contains(".")) {
            fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        } else {
            if (contentType.equals("image/jpeg")) {
                fileExtension = ".jpg";
            } else if (contentType.equals("image/png")) {
                fileExtension = ".png";
            } else if (contentType.equals("image/webp")) {
                fileExtension = ".webp";
            } else {
                fileExtension = ".jpg";
            }
        }

        String fileName = UUID.randomUUID().toString();
        String newFileName = fileName + fileExtension;

        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Path filePath = Paths.get(UPLOAD_DIR + newFileName);
        System.out.println("Guardando archivo en: " + filePath.toAbsolutePath().toString());

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo guardado correctamente: " + newFileName);
            return newFileName;
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
            throw new IOException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
}

