package com.workflow.document.service;

import com.workflow.document.domain.Document;
import com.workflow.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DocumentStorageService {

    private final DocumentRepository repository;
    private final String UPLOAD_DIR = "local_uploads/";

    public DocumentStorageService(DocumentRepository repository) {
        this.repository = repository;
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public Document storeFile(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, file.getBytes());

            Document doc = new Document();
            doc.setName(file.getOriginalFilename());
            doc.setSize(file.getSize());
            doc.setType(file.getContentType());
            doc.setFilePath(path.toString());
            doc.setStatus("Processed");

            return repository.save(doc);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file.", e);
        }
    }

    public java.util.List<Document> getAllDocuments() {
        return repository.findAll();
    }
}
