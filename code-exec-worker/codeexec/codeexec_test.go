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
	executor, err :=  codeexec.NewDockerExecutor("invalid-language")
	assert.NoError(t, err)

	// Define code that might cause an error
	code := `print("Hello from Python!")`

	output, err := executor.Execute(code)
	assert.Error(t, err)
	assert.Empty(t, output)
}
