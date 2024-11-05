package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.Problem;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProblemRepository extends CrudRepository<Problem, UUID> {

}
