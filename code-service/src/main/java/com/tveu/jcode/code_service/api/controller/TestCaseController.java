package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.TestCaseCreateRequest;
import com.tveu.jcode.code_service.api.dto.TestCaseDTO;
import com.tveu.jcode.code_service.api.dto.TestCaseUpdateRequest;
import com.tveu.jcode.code_service.core.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping(Path.TEST_CASE_GET)
    @ResponseStatus(HttpStatus.OK)
                                public List<TestCaseDTO> getTestCase(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String problemID) {

        if (!id.isEmpty()) {
            return List.of(testCaseService.get(id));
        }
        if (!problemID.isEmpty()) {
            return testCaseService.getByProblemID(problemID);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must one of the params");
    }

    @PostMapping(Path.TEST_CASE_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TestCaseDTO createTestCase(@RequestBody TestCaseCreateRequest createRequest) {

        return testCaseService.create(createRequest);
    }

    @PutMapping(Path.TEST_CASE_PUT)
    @ResponseStatus(HttpStatus.OK)
    public TestCaseDTO updateTestCase(@PathVariable String id, @RequestBody TestCaseUpdateRequest updateRequest) {

        return testCaseService.update(id, updateRequest);
    }

    @DeleteMapping(Path.TEST_CASE_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestCase(@PathVariable String id) {
        testCaseService.delete(id);
    }
}
