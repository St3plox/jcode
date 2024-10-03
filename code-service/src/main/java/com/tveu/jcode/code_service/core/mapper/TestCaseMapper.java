package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.core.entity.TestCase;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestCaseMapper {

    private final ProblemRepository problemRepository;

    public TestCase map(TestCaseCreateRequest createRequest) {

        var problem = problemRepository.findById(UUID.fromString(createRequest.problemID()))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "problem not found"));

        return TestCase.builder()
                .id(UUID.randomUUID())
                .input(createRequest.input())
                .output(createRequest.output())
                .problem(problem)
                .build();
    }

    public TestCaseDTO map(TestCase testCase) {

        return TestCaseDTO.builder()
                .id(testCase.getId().toString())
                .input(testCase.getInput())
                .output(testCase.getOutput())
                .problemID(testCase.getProblem().getId().toString())
                .build();
    }
}
