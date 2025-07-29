package com.example.gradingproject.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String address;
}