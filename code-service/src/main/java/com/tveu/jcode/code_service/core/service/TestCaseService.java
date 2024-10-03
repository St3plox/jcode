package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.api.dto.TestCaseUpdateRequest;

public interface TestCaseService {

    TestCaseDTO create(TestCaseCreateRequest createRequest);

    TestCaseDTO get(String id);

    TestCaseDTO update(String id, TestCaseUpdateRequest updateRequest);

    void delete(String id);
}
