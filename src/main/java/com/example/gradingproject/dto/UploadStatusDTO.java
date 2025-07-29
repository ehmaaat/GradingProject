package com.example.gradingproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadStatusDTO {
    private Long id;
    private String filename;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}