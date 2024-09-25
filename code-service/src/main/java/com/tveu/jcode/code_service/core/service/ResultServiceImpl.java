package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.ResultMapper;
import com.tveu.jcode.code_service.core.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final SubmissionService submissionService;

    @Override
    public ResultDTO create(ResultDTO resultDTO) {

        log.info("Input resultDTO: {}", resultDTO);
        var result = resultRepository.save(resultMapper.map(resultDTO));

        if (result.getErrors().isEmpty()) {
            submissionService.updateStatusByID(
                    result.getSubmission().getId().toString(),
                    SubmissionStatus.COMPLETED.toString()
            );
        } else {
            submissionService.updateStatusByID(
                    result.getSubmission().getId().toString(),
                    SubmissionStatus.FAILED.toString()
            );
        }

        log.debug("Created result: {}", result);
        return resultMapper.map(result);
    }

    @Override
    public ResultDTO getByID(UUID id) {

        var resultDTO = resultRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " Submission not found"));

        return resultMapper.map(resultDTO);
    }
}
