package com.tveu.jcode.code_service.core.repository;

import com.tveu.jcode.code_service.config.TestDatabaseConfig;
import com.tveu.jcode.code_service.core.entity.Problem;
import com.tveu.jcode.code_service.core.entity.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@Import(TestDatabaseConfig.class)
class TestCaseRepositoryTest {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ProblemRepository problemRepository;

    private Problem problem;
    private Problem anotherProblem;

    private List<TestCase> testCases;
    private TestCase anotherProblemTestCase;

    @BeforeEach
    void setUp() {
        problem = Problem.builder()
                .title("example")
                .description("description")
                .userId(1L)
                .build();

        anotherProblem = Problem.builder()
                .title("example-1")
                .description("description-1")
                .userId(1L)
                .build();

        testCases = List.of(
                TestCase.builder()
                        .problem(problem)
                        .output("1")
                        .input("1")
                        .build(),

                TestCase.builder()
                        .problem(problem)
                        .output("2")
                        .input("2")
                        .build(),

                TestCase.builder()
                        .problem(problem)
                        .output("3")
                        .input("3")
                        .build()

        );


        anotherProblemTestCase = TestCase.builder()
                .problem(anotherProblem)
                .output("4")
                .input("4")
                .build();
    }

    @Test
    void findAllByProblem_withValidInput_returnsListTestCases(){
        problemRepository.save(problem);
        problemRepository.save(anotherProblem);

        testCaseRepository.saveAll(testCases);
        testCaseRepository.save(anotherProblemTestCase);

        var result = testCaseRepository.findAllByProblem(problem);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(testCases.size(), result.size());
        Assertions.assertFalse(result.contains(anotherProblemTestCase));
    }

    @AfterEach
    void tearDown() {
        testCaseRepository.deleteAll();
        problemRepository.deleteAll();
    }
}