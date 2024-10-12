package consumer

import (
	"context"
	"os"
	"sync"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/google/uuid"
	"github.com/rs/zerolog"
)

type Controller struct {
	submissionConsumer        Consumer[broker.SubmissionDTO]
	problemSubmissionConsumer Consumer[broker.ProblemSubmissionKafkaDTO]
	producer                  producer.Producer
	delay                     time.Duration
	log                       *zerolog.Logger
	shutdown                  chan os.Signal
}

// ListenForEvents listens to events from Kafka and processes each event concurrently
func (c *Controller) ListenForEvents(ctx context.Context) error {

	var wg sync.WaitGroup

	wg.Add(1)
	go func() {
		defer wg.Done()
		if err := c.listenForSubmissionEvents(ctx); err != nil {
			c.log.Error().Err(err).Msg("Error processing submission events")
		}
	}()

	wg.Add(1)
	go func() {
		defer wg.Done()
		if err := c.listenForProblemSubmissionEvents(ctx); err != nil {
			c.log.Error().Err(err).Msg("Error processing problem submission events")
		}
	}()

	wg.Wait()
	return nil
}

func (c *Controller) listenForSubmissionEvents(ctx context.Context) error {

	submissionEventChannel, err := c.submissionConsumer.Consume(ctx)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to start consuming submission events")
		return err
	}

	for subEvent := range submissionEventChannel {
		c.processSubmissionEvent(subEvent)
	}
	return nil
}

func (c *Controller) listenForProblemSubmissionEvents(ctx context.Context) error {

	problemEventChannel, err := c.problemSubmissionConsumer.Consume(ctx)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to start consuming problem submission events")
		return err
	}

	for probEvent := range problemEventChannel {
		c.processProblemSubmissionEvent(probEvent)
	}
	return nil
}

// processSubmissionEvent processes the SubmissionDTO type event
func (c *Controller) processSubmissionEvent(subEvent broker.SubmissionDTO) {

	c.log.Info().Msg("Started processing SubmissionDTO event")

	events := make([]any, 1)

	exec, err := codeexec.NewDockerExecutor(subEvent.Language)
	if err != nil {
		events = append(events, broker.MapTo(subEvent, "", err.Error()))
		c.log.Error().Err(err).Msg("Failed to create code executor")
		c.producer.ProduceEvents(events)
		return
	}

	output, err := exec.Execute(subEvent.Code)
	if err != nil {
		events = append(events, broker.MapTo(subEvent, output, err.Error()))
		c.log.Error().Err(err).Msg("Failed to execute code")
		c.producer.ProduceEvents(events)
		return
	}

	events = append(events, broker.MapTo(subEvent, output, ""))
	c.producer.ProduceEvents(events)
	c.log.Info().Msg(subEvent.ID.String() + " processed \n output: " + output)
}

// processProblemSubmissionEvent processes the ProblemSubmissionKafkaDTO event
func (c *Controller) processProblemSubmissionEvent(problemEvent broker.ProblemSubmissionKafkaDTO) {

	c.log.Info().Msg("Started processing ProblemSubmissionKafkaDTO event")

	events := make([]any, 1)

	exec, err := codeexec.NewDockerExecutor(problemEvent.SubmissionDTO.Language)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to execute code")
		//TODO: add error response
		return
	}

	err = exec.RunTestCases(problemEvent.SubmissionDTO.Code, problemEvent.TestCases)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to execute code")
		//TODO: add error response
		return
	}

	result := broker.ResultDTO{
		ID:           uuid.NewString(),
		SubmissionID: problemEvent.SubmissionDTO.ID.String(),
		Output:       "Test passed",
		Errors:       "",
	}

	events = append(events, result)
	c.producer.ProduceEvents(events)
	c.log.Info().Msg(" processed \n output: ")

}
