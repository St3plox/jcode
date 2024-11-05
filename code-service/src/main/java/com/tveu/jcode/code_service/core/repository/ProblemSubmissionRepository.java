package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.ProblemSubmission;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProblemSubmissionRepository extends CrudRepository<ProblemSubmission, UUID> {
}
