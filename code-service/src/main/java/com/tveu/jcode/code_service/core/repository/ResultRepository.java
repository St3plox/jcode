package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.core.entity.Result;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ResultRepository extends CrudRepository<Result, UUID> {
}
