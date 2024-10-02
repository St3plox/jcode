package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.TestCase;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TestCaseRepository extends CrudRepository<TestCase, UUID> {
}
