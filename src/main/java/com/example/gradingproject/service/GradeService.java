package com.example.gradingproject.service;

import com.example.gradingproject.dto.GradeDTO;
import com.example.gradingproject.entity.Grade;
import com.example.gradingproject.repository.CourseRepository;
import com.example.gradingproject.repository.EnrollmentRepository;
import com.example.gradingproject.repository.GradeRepository;
import com.example.gradingproject.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;


    @Transactional
    public GradeDTO recordOrUpdateGrade(GradeDTO dto) {
        if (!enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new RuntimeException("Student is not enrolled in this course.");
        }

        Grade grade = gradeRepository.findByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())
                .orElse(Grade.builder()
                        .student(studentRepository.findById(dto.getStudentId())
                                .orElseThrow(() -> new RuntimeException("Student not found.")))
                        .course(courseRepository.findById(dto.getCourseId())
                                .orElseThrow(() -> new RuntimeException("Course not found.")))
                        .build());

        if (grade.isCourseCompleted()) {
            throw new RuntimeException("Cannot update grade. Course already marked as completed.");
        }

        grade.setValue(dto.getValue());
        grade.setCourseCompleted(dto.isCourseCompleted());
        Grade saved = gradeRepository.save(grade);

        return GradeDTO.builder()
                .studentId(saved.getStudent().getId())
                .courseId(saved.getCourse().getId())
                .value(saved.getValue())
                .courseCompleted(saved.isCourseCompleted())
                .build();
    }

    public List<GradeDTO> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(g -> GradeDTO.builder()
                        .studentId(g.getStudent().getId())
                        .courseId(g.getCourse().getId())
                        .value(g.getValue())
                        .courseCompleted(g.isCourseCompleted())
                        .build())
                .collect(Collectors.toList());
    }

    public double calculateGPA(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) return 0.0;

        double totalPoints = 0.0;
        for (Grade g : grades) {
            totalPoints += mapGradeToPoints(g.getValue());
        }
        return totalPoints / grades.size();
    }

    private double mapGradeToPoints(String gradeValue) {
        return switch (gradeValue.toUpperCase()) {
            case "A+" -> 4.0;
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }
}
