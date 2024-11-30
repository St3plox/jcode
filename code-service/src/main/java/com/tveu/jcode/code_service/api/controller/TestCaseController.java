package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.ApiResponse;
import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.api.dto.TestCaseUpdateRequest;
import com.tveu.jcode.code_service.core.service.TestCaseService;
import com.tveu.jcode.code_service.core.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping(Path.TEST_CASE_GET)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TestCaseDTO>> get(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String problemID) {

        if (!id.isEmpty()) {
            return ResponseUtil.success(
                    "Test case retrieved successfully",
                    List.of(testCaseService.get(id)),
                    null
            );
        }

        if (!problemID.isEmpty()) {
            var testCases = testCaseService.getByProblemID(problemID);
            return ResponseUtil.success(
                    "test cases retrieved successfully",
                    testCases,
                    Map.of("length", testCases.size())
            );
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user must one of the params");
    }

    @PostMapping(Path.TEST_CASE_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TestCaseDTO> create(@RequestBody TestCaseCreateRequest createRequest) {

        var testCase = testCaseService.create(createRequest);
        return ResponseUtil.success("test case created successfully", testCase, null);
    }

    @PutMapping(Path.TEST_CASE_PUT)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TestCaseDTO> update(@PathVariable String id, @RequestBody TestCaseUpdateRequest updateRequest) {

        var testCase = testCaseService.update(id, updateRequest);
        return ResponseUtil.success("test case updated successfully", testCase, null);
    }

    @DeleteMapping(Path.TEST_CASE_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Object> delete(@PathVariable String id) {

        testCaseService.delete(id);
        return ResponseUtil.success("test case deleted successfully", null, null);
    }
}
