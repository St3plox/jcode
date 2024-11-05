package service

import (
    "github.com/St3pegor/jcode/broker"
    "github.com/St3pegor/jcode/broker/producer"
    "github.com/St3pegor/jcode/codeexec"
    "github.com/rs/zerolog"
)

// SubmissionProcessor implements the Processor interface
type SubmissionProcessor struct {
    producer producer.Producer
    log      *zerolog.Logger
}

// NewSubmissionProcessor creates a new instance of SubmissionProcessor
func NewSubmissionProcessor(producer producer.Producer, log *zerolog.Logger) *SubmissionProcessor {
    return &SubmissionProcessor{
        producer: producer,
        log:      log,
    }
}

// Process processes a submission event
func (sp *SubmissionProcessor) Process(event interface{}) {
    subEvent, ok := event.(broker.SubmissionDTO)
    if !ok {
        sp.log.Error().Msg("Invalid event type for SubmissionProcessor")
        return
    }
    sp.log.Info().Msg("Started processing SubmissionDTO event")

    events := make([]any, 1)

    exec, err := codeexec.NewDockerExecutor(subEvent.Language)
    if err != nil {
        events = append(events, broker.MapTo(subEvent, "", err.Error()))
        sp.log.Error().Err(err).Msg("Failed to create code executor")
        sp.producer.ProduceEvents("result", events)
        return
    }

    output, err := exec.Execute(subEvent.Code)
    if err != nil {
        events = append(events, broker.MapTo(subEvent, output, err.Error()))
        sp.log.Error().Err(err).Msg("Failed to execute code")
        sp.producer.ProduceEvents("result", events)
        return
    }

    events = append(events, broker.MapTo(subEvent, output, ""))
    sp.producer.ProduceEvents("result", events)
    sp.log.Info().Msg(subEvent.ID.String() + " processed \n output: " + output)
}
