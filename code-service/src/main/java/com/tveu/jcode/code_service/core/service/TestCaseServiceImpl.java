package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.api.dto.TestCaseUpdateRequest;
import com.tveu.jcode.code_service.core.entity.TestCase;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.TestCaseMapper;
import com.tveu.jcode.code_service.core.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

    @Override
    public TestCaseDTO create(TestCaseCreateRequest createRequest) {

        var testCase = testCaseMapper.map(createRequest);
        var savedTestCase = testCaseRepository.save(testCase);

        return testCaseMapper.map(savedTestCase);
    }

    @Override
    public TestCaseDTO get(String id) {

        var testCase = testCaseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Can't find test case with id: " + id));

        return testCaseMapper.map(testCase);
    }

    @Override
    public TestCaseDTO update(String id, TestCaseUpdateRequest updateRequest) {

        var testCase = testCaseRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Can't find test case with id: " + id));

        if (updateRequest.input() != null && updateRequest.input().isEmpty()) {
            testCase.setInput(updateRequest.input());
        }
        if (updateRequest.output() != null && !updateRequest.output().isEmpty()) {
            testCase.setOutput(updateRequest.output());
        }

        var savedTestCase = testCaseRepository.save(testCase);
        return testCaseMapper.map(savedTestCase);
    }

    @Override
    public void delete(String id) {
        testCaseRepository.deleteById(UUID.fromString(id));
    }
}
