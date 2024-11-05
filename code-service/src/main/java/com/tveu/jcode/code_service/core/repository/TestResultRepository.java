package com.tveu.jcode.code_service.core.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.tveu.jcode.code_service.core.entity.TestResult;
public interface TestResultRepository extends CrudRepository<TestResult, UUID> {

}
