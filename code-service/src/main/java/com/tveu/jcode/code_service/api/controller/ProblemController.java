package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.ApiResponse;
import com.tveu.jcode.code_service.api.dto.ProblemCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemDTO;
import com.tveu.jcode.code_service.api.dto.ProblemUpdateRequest;
import com.tveu.jcode.code_service.core.service.ProblemService;
import com.tveu.jcode.code_service.core.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping(Path.PROBLEM_GET)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProblemDTO> get(@PathVariable String id) {

        var problem = problemService.get(id);

        return ResponseUtil.success("Problem retrieved successfully", problem, null);
    }

    @PostMapping(Path.PROBLEM_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProblemDTO> create(@RequestBody @Valid ProblemCreateRequest createRequest) {

        var problem = problemService.create(createRequest);
        return ResponseUtil.success("problem saved successfully", problem, null);
    }

    @PutMapping(Path.PROBLEM_PUT)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProblemDTO> update(@PathVariable String id, @RequestBody @Valid ProblemUpdateRequest updateRequest) {

        var problem = problemService.update(id, updateRequest);
        return ResponseUtil.success("problem updated successfully", problem, null);
    }

    @DeleteMapping(Path.PROBLEM_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Object> delete(@PathVariable String id) {

        problemService.delete(id);
        return ResponseUtil.success("problem deleted successfully", null, null);
    }
}
