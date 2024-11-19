package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SubmissionRepository  extends JpaRepository<Submission, UUID> {
}
