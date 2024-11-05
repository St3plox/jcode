package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.core.entity.ProblemResult;

public interface ProblemResultService {

    ProblemResult create(ProblemResultDTO createRequest);
}
