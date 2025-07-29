package com.example.gradingproject.service;

import com.example.gradingproject.dto.UploadStatusDTO;
import com.example.gradingproject.entity.Student;
import com.example.gradingproject.entity.UploadStatus;
import com.example.gradingproject.repository.StudentRepository;
import com.example.gradingproject.repository.UploadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UploadService {

    @Autowired
    UploadStatusRepository uploadStatusRepository;
    @Autowired
    StudentRepository studentRepository;

    @Async
    public void processCSV(MultipartFile file, UploadStatus uploadStatus) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // skip headers
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Student student = Student.builder()
                        .name(values[0])
                        .email(values[1])
                        .dob(LocalDate.parse(values[2]))
                        .address(values[3])
                        .build();
                if (!studentRepository.existsByEmail(student.getEmail())) {
                    studentRepository.save(student);
                }
            }
            uploadStatus.setStatus("COMPLETED");
            uploadStatus.setCompletedAt(LocalDateTime.now());
        } catch (Exception e) {
            uploadStatus.setStatus("FAILED");
            uploadStatus.setCompletedAt(LocalDateTime.now());
        } finally {
            uploadStatusRepository.save(uploadStatus);
        }
    }

    public UploadStatusDTO getStatus(Long id) {
        return uploadStatusRepository.findById(id)
                .map(s -> UploadStatusDTO.builder()
                        .id(s.getId())
                        .filename(s.getFilename())
                        .status(s.getStatus())
                        .startedAt(s.getStartedAt())
                        .completedAt(s.getCompletedAt())
                        .build())
                .orElseThrow(() -> new RuntimeException("Status not found"));
    }

    public UploadStatus createStatus(String filename) {
        UploadStatus status = UploadStatus.builder()
                .filename(filename)
                .status("IN_PROGRESS")
                .startedAt(LocalDateTime.now())
                .build();
        return uploadStatusRepository.save(status);
    }
}

