package com.example.gradingproject.service;


import com.example.gradingproject.dto.StudentDTO;
import com.example.gradingproject.entity.Student;
import com.example.gradingproject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudent(Long id) {
        return studentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Student student = studentRepository.save(mapToEntity(dto));
        return mapToDto(student);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setName(dto.getName());
        student.setDob(dto.getDob());
        student.setAddress(dto.getAddress());
        return mapToDto(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    private StudentDTO mapToDto(Student s) {
        return StudentDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .email(s.getEmail())
                .dob(s.getDob())
                .address(s.getAddress())
                .build();
    }

    private Student mapToEntity(StudentDTO d) {
        return Student.builder()
                .name(d.getName())
                .email(d.getEmail())
                .dob(d.getDob())
                .address(d.getAddress())
                .build();
    }
}
