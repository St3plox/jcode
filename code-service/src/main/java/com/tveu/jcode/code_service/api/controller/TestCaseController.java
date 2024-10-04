package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Paths;
import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.api.dto.TestCaseUpdateRequest;
import com.tveu.jcode.code_service.core.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping(Paths.TEST_CASE_GET)
    @ResponseStatus(HttpStatus.OK)
    public TestCaseDTO getTestCase(@PathVariable String id) {

        return testCaseService.get(id);
    }

    @GetMapping(Paths.TEST_CASE_GET_BY_PROBLEM)
    @ResponseStatus(HttpStatus.OK)
    public List<TestCaseDTO> getTestCaseByProblem(@PathVariable String id) {

        return testCaseService.getByProblemID(id);
    }

    @PostMapping(Paths.TEST_CASE_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TestCaseDTO createTestCase(@RequestBody TestCaseCreateRequest createRequest) {

        return testCaseService.create(createRequest);
    }

    @PutMapping(Paths.TEST_CASE_PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TestCaseDTO updateTestCase(@PathVariable String id, @RequestBody TestCaseUpdateRequest updateRequest) {

        return testCaseService.update(id, updateRequest);
    }

    @DeleteMapping(Paths.TEST_CASE_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestCase(@PathVariable String id) {
        testCaseService.delete(id);
    }
}
