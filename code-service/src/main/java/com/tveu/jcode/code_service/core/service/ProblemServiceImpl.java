package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ProblemCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemDTO;
import com.tveu.jcode.code_service.api.dto.ProblemUpdateRequest;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.ProblemMapper;
import com.tveu.jcode.code_service.core.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    @Override
    public ProblemDTO create(ProblemCreateRequest createRequest) {

        var problem = problemRepository.save(problemMapper.map(createRequest));
        return problemMapper.map(problem);
    }

    @Override
    public ProblemDTO get(String id) {

        var problem = problemRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Problem not found"));

        return problemMapper.map(problem);
    }

    @Override
    public ProblemDTO update(String id, ProblemUpdateRequest problemUpdate) {

        var problem = problemRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Problem not found"));

        if (!problemUpdate.title().isEmpty()) {
            problem.setTitle(problemUpdate.title());
        }

        if (!problemUpdate.description().isEmpty()) {
            problem.setTitle(problemUpdate.description());
        }

        return problemMapper.map(problemRepository.save(problem));
    }

    @Override
    public void delete(String id) {
        problemRepository.deleteById(UUID.fromString(id));
    }
}
