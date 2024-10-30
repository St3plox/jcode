package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.ProblemCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemDTO;
import com.tveu.jcode.code_service.api.dto.ProblemUpdateRequest;
import com.tveu.jcode.code_service.core.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping(Path.PROBLEM_GET)
    @ResponseStatus(HttpStatus.OK)
    public ProblemDTO getProblem(@PathVariable String id) {

        return problemService.get(id);
    }

    @PostMapping(Path.PROBLEM_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProblemDTO createProblem(@RequestBody ProblemCreateRequest createRequest) {

        return problemService.create(createRequest);
    }

    @PutMapping(Path.PROBLEM_PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProblemDTO updateProblem(@PathVariable String id, @RequestBody ProblemUpdateRequest updateRequest) {

        return problemService.update(id, updateRequest);
    }

    @DeleteMapping(Path.PROBLEM_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProblem(@PathVariable String id) {

        problemService.delete(id);
    }
}
