package com.example.gradingproject.controller;

import com.example.gradingproject.dto.GradeDTO;
import com.example.gradingproject.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    @Autowired
    GradeService gradeService;

    @PostMapping
    public ResponseEntity<GradeDTO> recordGrade(@RequestBody GradeDTO dto) {
        return ResponseEntity.ok(gradeService.recordOrUpdateGrade(dto));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDTO>> getStudentGrades(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/gpa")
    public ResponseEntity<Double> getStudentGPA(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.calculateGPA(studentId));
    }
}
