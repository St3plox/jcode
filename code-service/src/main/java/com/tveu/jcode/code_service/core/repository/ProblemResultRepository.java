package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.ProblemResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProblemResultRepository extends JpaRepository<ProblemResult, UUID> {

}
