package producer

import "github.com/google/uuid"

type ResultDTO struct {
	ID           uuid.UUID `json:"id"`
	SubmissionID uuid.UUID `json:"submission_id"`
	Output       string    `json:"output"`
	Errors       string    `json:"errors"`
}
