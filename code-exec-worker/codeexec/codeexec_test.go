package codeexec_test

import (
	"testing"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/stretchr/testify/assert"
)

// TestDockerExecutor tests the DockerExecutor.
func TestDockerExecutor(t *testing.T) {
	executor, err := codeexec.NewDockerExecutor(broker.PYTHON)
	assert.NoError(t, err)

	// Define a simple Python code to execute
	code := `print("Hello from Python!")`

	output, err := executor.Execute(code)
	assert.NoError(t, err)
	assert.Contains(t, output, "Hello from Python!")
}

// TestDockerExecutor_Error tests error handling in DockerExecutor.
func TestDockerExecutor_Error(t *testing.T) {
	executor, err := codeexec.NewDockerExecutor("invalid-language")
	assert.NoError(t, err)

	code := `print("Hello from Python!")`

	output, err := executor.Execute(code)
	assert.Error(t, err)
	assert.Empty(t, output)
}

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
	err = executor.RunTestCases(code, testCases)
	assert.NoError(t, err)
}

// TestRunTestCases_Failure tests the RunTestCases function with an expected failure.
func TestRunTestCases_Failure(t *testing.T) {
	// Initialize the DockerExecutor for Python.
	executor, err := codeexec.NewDockerExecutor(broker.PYTHON)
	assert.NoError(t, err)

	// Define a simple Python code that reads input and prints a result.
	code := `
input_data = input()
print(f'Hello, {input_data}')
`

	// Define test cases with one incorrect expected output.
	testCases := []broker.TestCaseDTO{
		{ID: "1", ProblemID: "101", Input: "World", Output: "Hello, World\n"},
		{ID: "2", ProblemID: "102", Input: "Python", Output: "Hello, Java\n"}, // Intentional mismatch
	}

	// Execute the test cases using the DockerExecutor and expect an error.
	err = executor.RunTestCases(code, testCases)
	assert.Error(t, err)
}
