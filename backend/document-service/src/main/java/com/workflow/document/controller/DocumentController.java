package com.workflow.document.controller;

import com.workflow.document.domain.Document;
import com.workflow.document.service.DocumentStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentStorageService storageService;

    public DocumentController(DocumentStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file) {
        Document doc = storageService.storeFile(file);
        return ResponseEntity.ok(doc);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(storageService.getAllDocuments());
    }
}
