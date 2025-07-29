package com.example.gradingproject.service;

import com.example.gradingproject.dto.EnrollmentDTO;
import com.example.gradingproject.entity.Course;
import com.example.gradingproject.entity.Enrollment;
import com.example.gradingproject.entity.Student;
import com.example.gradingproject.repository.CourseRepository;
import com.example.gradingproject.repository.EnrollmentRepository;
import com.example.gradingproject.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Transactional
    public void enrollStudent(EnrollmentDTO dto) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new RuntimeException("Student is already enrolled in the course.");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found."));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found."));

        // Check course capacity
        long enrolledCount = enrollmentRepository.countByCourseId(course.getId());
        if (enrolledCount >= course.getCapacity()) {
            throw new RuntimeException("Course is full.");
        }

        // Check prerequisites
        Set<Long> enrolledCourseIds = enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());

        boolean hasAllPrerequisites = course.getPrerequisites().stream()
                .allMatch(prereq -> enrolledCourseIds.contains(prereq.getId()));

        if (!hasAllPrerequisites) {
            throw new RuntimeException("Student has not met course prerequisites.");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrolledAt(LocalDate.now())
                .build();

        enrollmentRepository.save(enrollment);
    }
}