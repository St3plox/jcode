package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ResultDTO;

import java.util.UUID;

public interface ResultService {

    ResultDTO create(ResultDTO createRequest);

    ResultDTO getByID(UUID id);
}
