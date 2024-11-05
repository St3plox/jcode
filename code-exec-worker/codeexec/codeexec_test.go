package codeexec_test

import (
    "testing"

    "github.com/St3pegor/jcode/broker"
    "github.com/St3pegor/jcode/codeexec"
    "github.com/stretchr/testify/assert"
)

// TestRunTestCases_Success tests the RunTestCases function with actual Docker execution.
func TestRunTestCases_Success(t *testing.T) {
    // Initialize the DockerExecutor for Python.
    executor, err := codeexec.NewDockerExecutor(broker.PYTHON)
    assert.NoError(t, err)

    // Define a simple Python code that reads input and prints a result.
    code := `input_data = input()
print(f'Hello, {input_data}')
`

    // Define test cases with expected inputs and outputs.
    testCases := []broker.TestCaseDTO{
        {ID: "1", ProblemID: "101", Input: "World", Output: "Hello, World\n"},
        {ID: "2", ProblemID: "102", Input: "Python", Output: "Hello, Python\n"},
    }

    // Execute the test cases using the DockerExecutor.
    results := executor.RunTestCases(code, testCases)

    // Assert that all results are successful and have the correct outputs.
    assert.Equal(t, len(testCases), len(results))
    for i, result := range results {
        assert.Equal(t, testCases[i].ID, result.TestCaseID)
        assert.True(t, result.IsSuccessful, "Test case %s failed unexpectedly", result.TestCaseID)
        assert.Equal(t, testCases[i].Output, result.Output)
    }
}

// TestRunTestCases_Failure tests the RunTestCases function with an expected failure.
func TestRunTestCases_Failure(t *testing.T) {
    // Initialize the DockerExecutor for Python.
    executor, err := codeexec.NewDockerExecutor(broker.PYTHON)
    assert.NoError(t, err)

    // Define a simple Python code that reads input and prints a result.
    code := `input_data = input()
print(f'Hello, {input_data}')
`

    // Define test cases with one incorrect expected output.
    testCases := []broker.TestCaseDTO{
        {ID: "1", ProblemID: "101", Input: "World", Output: "Hello, World\n"},
        {ID: "2", ProblemID: "102", Input: "Python", Output: "Hello, Java\n"}, // Intentional mismatch
    }

    // Execute the test cases using the DockerExecutor.
    results := executor.RunTestCases(code, testCases)

    // Assert that results are returned and check for failures.
    assert.Equal(t, len(testCases), len(results))
    for i, result := range results {
        assert.Equal(t, testCases[i].ID, result.TestCaseID)
        if testCases[i].Output != result.Output {
            assert.False(t, result.IsSuccessful, "Test case %s should have failed", result.TestCaseID)
        } else {
            assert.True(t, result.IsSuccessful, "Test case %s passed unexpectedly", result.TestCaseID)
        }
    }
}
