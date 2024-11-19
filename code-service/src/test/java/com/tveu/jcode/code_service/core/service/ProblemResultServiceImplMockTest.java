package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.api.dto.TestResultDTO;
import com.tveu.jcode.code_service.core.entity.*;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.TestResultMapper;
import com.tveu.jcode.code_service.core.repository.ProblemResultRepository;
import com.tveu.jcode.code_service.core.repository.ProblemSubmissionRepository;
import com.tveu.jcode.code_service.core.repository.TestResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProblemResultServiceImplMockTest {

    @Mock
    private ProblemResultRepository problemResultRepository;

    @Mock
    private TestResultRepository testResultRepository;

    @Mock
    private ProblemSubmissionRepository submissionRepository;

    @Mock
    private TestResultMapper testResultMapper;

    @InjectMocks
    private ProblemResultServiceImpl problemResultService;

    private ProblemResultDTO problemResultDTO;
    private ProblemSubmission mockSubmission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Result mockResult = Result.builder()
                .id(UUID.randomUUID())
                .output("Mocked Result Output")
                .errors("")
                .createdAt(LocalDateTime.now())
                .build();


        mockSubmission = ProblemSubmission.builder()
                .id(UUID.randomUUID())
                .submissionStatus(SubmissionStatus.PENDING)
                .result(mockResult)
                .build();


        mockResult.setSubmission(mockSubmission);


        TestResultDTO testResultDTO = TestResultDTO.builder()
                .testCaseID(UUID.randomUUID().toString())
                .isSuccessful(true)
                .output("Test Output")
                .build();

        ResultDTO resultDTO = ResultDTO.builder()
                .id(UUID.randomUUID().toString())
                .submissionID(mockSubmission.getId().toString())
                .errors("")
                .output("All Tests Passed")
                .build();

        problemResultDTO = ProblemResultDTO.builder()
                .resultDTO(resultDTO)
                .testResults(List.of(testResultDTO))
                .build();
    }

    @Test
    void createProblemResult_withValidInput_savesAndReturnsProblemResult() {

        when(submissionRepository.findById(UUID.fromString(problemResultDTO.resultDTO().submissionID())))
                .thenReturn(Optional.of(mockSubmission));

        ProblemResult mockProblemResult = new ProblemResult();
        mockProblemResult.setId(UUID.randomUUID());
        mockProblemResult.setOutput(problemResultDTO.resultDTO().output());
        mockProblemResult.setErrors(problemResultDTO.resultDTO().errors());
        mockProblemResult.setSubmission(mockSubmission);

        when(problemResultRepository.save(any(ProblemResult.class)))
                .thenReturn(mockProblemResult);


        ProblemResult result = problemResultService.create(problemResultDTO);


        assertThat(result).isNotNull();
        assertThat(result.getOutput()).isEqualTo("All Tests Passed");
        assertThat(result.getErrors()).isEmpty();
        verify(testResultRepository).saveAll(anyList());
        verify(problemResultRepository).save(any(ProblemResult.class));
        verify(submissionRepository).save(mockSubmission);
    }

    @Test
    void createProblemResult_withInvalidSubmission_throwsServiceException() {

        when(submissionRepository.findById(UUID.fromString(problemResultDTO.resultDTO().submissionID())))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> problemResultService.create(problemResultDTO))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Submission not found");

        verify(problemResultRepository, never()).save(any(ProblemResult.class));
    }
}
