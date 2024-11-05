package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ProblemSubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionDTO;

public interface ProblemSubmissionService {

    ProblemSubmissionDTO submit(ProblemSubmissionCreateRequest createRequest);

    ProblemSubmissionDTO getByID(String id);

    ProblemSubmissionDTO updateStatusByID(String id, String status);
}
