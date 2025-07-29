package com.example.gradingproject.controller;

import com.example.gradingproject.dto.UploadStatusDTO;
import com.example.gradingproject.entity.UploadStatus;
import com.example.gradingproject.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping
    public ResponseEntity<UploadStatusDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        UploadStatus status = uploadService.createStatus(file.getOriginalFilename());
        uploadService.processCSV(file, status);
        return ResponseEntity.ok(UploadStatusDTO.builder()
                .id(status.getId())
                .filename(status.getFilename())
                .status(status.getStatus())
                .startedAt(status.getStartedAt())
                .build());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<UploadStatusDTO> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(uploadService.getStatus(id));
    }
}