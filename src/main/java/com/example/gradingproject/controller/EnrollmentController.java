package com.example.gradingproject.controller;

import com.example.gradingproject.dto.EnrollmentDTO;
import com.example.gradingproject.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<String> enroll(@RequestBody EnrollmentDTO dto) {
        enrollmentService.enrollStudent(dto);
        return ResponseEntity.ok("Enrollment successful");
    }
}

