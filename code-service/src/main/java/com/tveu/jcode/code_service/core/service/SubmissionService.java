package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;

import java.util.UUID;

public interface SubmissionService {

    SubmissionDTO submit(SubmissionCreateRequest createRequest);

    SubmissionDTO getByID (UUID id);

    SubmissionDTO updateStatusByID (UUID id, SubmissionStatus status);
}
