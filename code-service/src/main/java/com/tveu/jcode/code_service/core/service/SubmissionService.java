package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;

public interface SubmissionService {

    SubmissionDTO submit(SubmissionCreateRequest createRequest);

    SubmissionDTO getByID(String id);

    SubmissionDTO updateStatusByID(String id, String status);
}
