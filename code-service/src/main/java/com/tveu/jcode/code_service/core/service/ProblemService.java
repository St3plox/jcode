package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.ProblemCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemDTO;
import com.tveu.jcode.code_service.api.dto.ProblemUpdateRequest;

public interface ProblemService {

    ProblemDTO create(ProblemCreateRequest createRequest);

    ProblemDTO get(String id);

    ProblemDTO update(String id, ProblemUpdateRequest problem);

    void delete(String id);
}
