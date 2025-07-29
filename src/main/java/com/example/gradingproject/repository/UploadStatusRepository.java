package com.example.gradingproject.repository;

import com.example.gradingproject.entity.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadStatusRepository extends JpaRepository<UploadStatus, Long> {
}
