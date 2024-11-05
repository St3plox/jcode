package codeexec

import (
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/google/uuid"
)

// RunTestCases executes a list of test cases and checks the output.
func (de *DockerExecutor) RunTestCases(code string, testCases []broker.TestCaseDTO) []broker.TestResultDTO {
	var results []broker.TestResultDTO

	for _, testCase := range testCases {
		output, err := de.ExecuteWithInputs(code, testCase.Input)
		isSuccessful := true

		if err != nil {
			isSuccessful = false
		} else if output != testCase.Output {
			isSuccessful = false
		}

		result := broker.TestResultDTO{
			ID:           uuid.NewString(),
			TestCaseID:   testCase.ID,
			IsSuccessful: isSuccessful,
			Output:       output,
			CreatedAt:    time.Now().Format(time.RFC3339),
		}
		results = append(results, result)
	}

	return results
}
