package service

import (
	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/google/uuid"
	"github.com/rs/zerolog"
)

// ProblemSubmissionProcessor implements the Processor interface
type ProblemSubmissionProcessor struct {
	producer producer.Producer
	log      *zerolog.Logger
}

// NewProblemSubmissionProcessor creates a new instance of ProblemSubmissionProcessor
func NewProblemSubmissionProcessor(producer producer.Producer, log *zerolog.Logger) *ProblemSubmissionProcessor {
	return &ProblemSubmissionProcessor{
		producer: producer,
		log:      log,
	}
}

//TODO: transfer ResultDTO creation to broker
// Process processes a problem submission event
func (psp *ProblemSubmissionProcessor) Process(event interface{}) {

	problemEvent, ok := event.(broker.ProblemSubmissionKafkaDTO)
	if !ok {
		psp.log.Error().Msg("Invalid event type for ProblemSubmissionProcessor")
		return
	}
	psp.log.Info().Msg("Started processing ProblemSubmissionKafkaDTO event")

	exec, err := codeexec.NewDockerExecutor(problemEvent.SubmissionDTO.Language)
	if err != nil {
		psp.log.Error().Err(err).Msg("Failed to execute code")

		result := broker.ResultDTO{
			ID:           uuid.NewString(),
			SubmissionID: problemEvent.SubmissionDTO.ID.String(),
			Output:       "",
			Errors:       "Failed to launch code executor",
		}

		dto := broker.ProblemResultDTO{
			ResultDTO:   result,
			TestResults: []broker.TestResultDTO{},
		}

		events := []any{dto}
		psp.producer.ProduceEvents("problem", events)
		psp.log.Info().Msg(problemEvent.SubmissionDTO.ID.String() + " processed")
		return
	}

	testResults := exec.RunTestCases(problemEvent.SubmissionDTO.Code, problemEvent.TestCases)

	result := broker.ResultDTO{
		ID:           uuid.NewString(),
		SubmissionID: problemEvent.SubmissionDTO.ID.String(),
		Output:       "Test passed",
		Errors:       "",
	}

	dto := broker.ProblemResultDTO{
		ResultDTO:   result,
		TestResults: testResults,
	}

	events := []any{dto}
	psp.producer.ProduceEvents("problem", events)
	psp.log.Info().Msg(problemEvent.SubmissionDTO.ID.String() + " processed")
}
