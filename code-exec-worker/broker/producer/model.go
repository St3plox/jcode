package producer

import (
	"github.com/St3pegor/jcode/broker"
	"github.com/google/uuid"
)

type ResultDTO struct {
	ID           string `json:"id"`
	SubmissionID string `json:"submission_id"`
	Output       string `json:"output"`
	Errors       string `json:"errors"`
}

func MapTo(subEvent broker.SubmissionDTO, output string, errors string) ResultDTO {

	return ResultDTO{
		ID:           uuid.New().String(),
		SubmissionID: subEvent.ID.String(),
		Output:       output,
		Errors:       errors,
	}
}
