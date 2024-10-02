package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.ProblemCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemDTO;
import com.tveu.jcode.code_service.core.entity.Problem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProblemMapper {

    public Problem map(ProblemCreateRequest createRequest) {

        return Problem.builder()
                .userId(createRequest.userId())
                .title(createRequest.title())
                .description(createRequest.description())
                .id(UUID.randomUUID())
                .build();
    }

    public ProblemDTO map(Problem problem) {

        return ProblemDTO.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .build();
    }
}
