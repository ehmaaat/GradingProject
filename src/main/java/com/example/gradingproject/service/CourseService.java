package com.example.gradingproject.service;

import com.example.gradingproject.dto.CourseDTO;
import com.example.gradingproject.entity.Course;
import com.example.gradingproject.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourse(Long id) {
        return courseRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        Set<Course> prerequisites = new HashSet<>();
        if (dto.getPrerequisiteIds() != null) {
            prerequisites = new HashSet<>(courseRepository.findAllById(dto.getPrerequisiteIds()));
        }
        Course course = Course.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .capacity(dto.getCapacity())
                .prerequisites(prerequisites)
                .build();
        return mapToDto(courseRepository.save(course));
    }

    private CourseDTO mapToDto(Course c) {
        return CourseDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .capacity(c.getCapacity())
                .prerequisiteIds(
                        c.getPrerequisites().stream().map(Course::getId).collect(Collectors.toSet())
                )
                .build();
    }
}
