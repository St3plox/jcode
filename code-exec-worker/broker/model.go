package broker

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
)

type Language string

const (
	GO     Language = "GO"
	JAVA   Language = "JAVA"
	PYTHON Language = "PYTHON"
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

// Custom time layout for parsing timestamps without timezones
const timeLayout = "2006-01-02T15:04:05.999999"

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
