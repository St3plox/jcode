package broker

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
)

type Language string

const (
	GO         Language = "GO"
	JAVA       Language = "JAVA"
	PYTHON     Language = "PYTHON"
	timeLayout          = "2006-01-02T15:04:05.999999"
)

type SubmissionStatus string

const (
	PENDING   SubmissionStatus = "PENDING"
	FAILED    SubmissionStatus = "FAILED"
	COMPLETED SubmissionStatus = "COMPLETED"
)

type SubmissionDTO struct {
	ID               uuid.UUID        `json:"id"`
	UserID           int64            `json:"userID"`
	Code             string           `json:"code"`
	Language         Language         `json:"language"`
	SubmissionStatus SubmissionStatus `json:"submissionStatus"`
	CreatedAt        time.Time        `json:"createdAt"`
	UpdatedAt        time.Time        `json:"updatedAt"`
}

type TestCaseDTO struct {
	ID        string `json:"id"`
	ProblemID string `json:"problemID"`
	Input     string `json:"input"`
	Output    string `json:"output"`
}

type ProblemSubmissionDTO struct {
	SubmissionDTO
	ProblemID string `json:"problemID"`
}

type ProblemSubmissionKafkaDTO struct {
	SubmissionDTO ProblemSubmissionDTO `json:"submission_dto"`
	TestCases     []TestCaseDTO        `json:"test_cases"`
}

type ResultDTO struct {
	ID           string `json:"id"`
	SubmissionID string `json:"submission_id"`
	Output       string `json:"output"`
	Errors       string `json:"errors"`
}

type TestResultDTO struct {
	ID           string `json:"id"`
	TestCaseID   string `json:"test_case_id"`
	IsSuccessful bool   `json:"is_successful"`
	Output       string `json:"output"`
	CreatedAt    string `json:"created_at"`
}

type ProblemResultDTO struct {
	ResultDTO   ResultDTO       `json:"result_dto"`
	TestResults []TestResultDTO `json:"test_case_results"`
}

func MapTo(subEvent SubmissionDTO, output string, errors string) ResultDTO {

	return ResultDTO{
		ID:           uuid.New().String(),
		SubmissionID: subEvent.ID.String(),
		Output:       output,
		Errors:       errors,
	}
}

// Custom UnmarshalJSON to handle time parsing
func (s *SubmissionDTO) UnmarshalJSON(data []byte) error {
	type Alias SubmissionDTO
	aux := &struct {
		CreatedAt string `json:"createdAt"`
		UpdatedAt string `json:"updatedAt"`
		*Alias
	}{
		Alias: (*Alias)(s),
	}

	if err := json.Unmarshal(data, &aux); err != nil {
		return fmt.Errorf("error unmarshaling SubmissionDTO %w", err)
	}

	createdAt, err := time.Parse(timeLayout, aux.CreatedAt)
	if err != nil {
		return fmt.Errorf("failed to parse createdAt %w", err)
	}
	s.CreatedAt = createdAt

	updatedAt, err := time.Parse(timeLayout, aux.UpdatedAt)
	if err != nil {
		return fmt.Errorf("failed to parse updatedAt %w", err)
	}
	s.UpdatedAt = updatedAt

	return nil
}
