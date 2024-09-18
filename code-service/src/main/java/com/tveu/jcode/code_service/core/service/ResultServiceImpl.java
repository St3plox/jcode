package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.ResultMapper;
import com.tveu.jcode.code_service.core.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;

    @Override
    public ResultDTO create(ResultDTO resultDTO) {

        var result = resultRepository.save(resultMapper.map(resultDTO));
        return resultMapper.map(result);
    }

    @Override
    public ResultDTO getByID(UUID id) {

        var resultDTO = resultRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " Submission not found"));

        return resultMapper.map(resultDTO);
    }
}
