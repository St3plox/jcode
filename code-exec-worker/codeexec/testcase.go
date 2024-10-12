package codeexec

import (
	"fmt"
	"log"

	"github.com/St3pegor/jcode/broker"
)

// RunTestCases executes a list of test cases and checks the output.
func (de *DockerExecutor) RunTestCases(code string, testCases []broker.TestCaseDTO) error {

	for idx, testCase := range testCases {
		output, err := de.ExecuteWithInputs(code, testCase.Input)
		if err != nil {
			return fmt.Errorf("test case %d failed with error: %w", idx+1, err)
		}

		// Compare the output with the expected result
		if output != testCase.Output {
			return fmt.Errorf("test case %d failed: expected '%s', but got '%s'", idx+1, testCase.Output, output)
		}

		log.Printf("Test case %d passed", idx+1)
	}

	return nil
}
