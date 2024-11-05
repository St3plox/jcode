package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.TestResultDTO;
import com.tveu.jcode.code_service.core.entity.TestResult;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestResultMapper {

    private final TestCaseRepository testCaseRepository;

    public TestResult map(TestResultDTO dto) {
        var testCase = testCaseRepository.findById(UUID.fromString(dto.testCaseID()))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Test case not found"));

        return new TestResult(
                UUID.randomUUID(),
                testCase,
                dto.isSuccessful(),
                dto.output(),
                LocalDateTime.now()
        );
    }

    public TestResultDTO map(TestResult testResult) {
        return TestResultDTO.builder()
                .id(testResult.getId().toString())
                .testCaseID(testResult.getTestCase().getId().toString())
                .isSuccessful(testResult.getIsSuccessful())
                .output(testResult.getOutput())
                .createdAt(testResult.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    public List<TestResult> map(List<TestResultDTO> testResultDTOS) {

        List<TestResult> results = new ArrayList<>();
        testResultDTOS.forEach(testResult -> results.add(map(testResult)));
        return results;
    }
}
