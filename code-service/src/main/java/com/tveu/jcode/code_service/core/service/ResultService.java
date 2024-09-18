package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ResultCreateRequest;
import com.tveu.jcode.code_service.api.dto.ResultDTO;

public interface ResultService {

    ResultDTO create(ResultCreateRequest createRequest);

    ResultDTO getByID(Long id);
}
