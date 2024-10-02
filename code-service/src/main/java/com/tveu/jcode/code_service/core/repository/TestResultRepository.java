package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.TestResult;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TestResultRepository extends CrudRepository<TestResult, UUID> {
}
