package consumer

import (
	"context"
	"os"
	"sync"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/rs/zerolog"
)

type Controller[T any] struct {
	consumer Consumer[T]
	producer producer.Producer
	delay    time.Duration
	log      *zerolog.Logger
	shutdown chan os.Signal
}

func NewController[T any](consumer Consumer[T], producer producer.Producer, delay time.Duration, log *zerolog.Logger, shutdown chan os.Signal) *Controller[T] {
	return &Controller[T]{
		consumer: consumer,
		producer: producer,
		delay:    delay,
		log:      log,
		shutdown: shutdown,
	}
}

// ListenForEvents listens to events from Kafka and processes each event concurrently
func (c *Controller[T]) ListenForEvents(ctx context.Context) error {
	eventChannel, err := c.consumer.Consume(ctx)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to start consuming events")
		return err
	}

	var wg sync.WaitGroup

	for event := range eventChannel {
		wg.Add(1)

		go func(event T) {
			defer wg.Done()

			// Type switch to handle different event types
			switch evt := any(event).(type) {
			case broker.SubmissionDTO:
				c.processSubmissionEvent(evt)
			case broker.ProblemSubmissionKafkaDTO:
				c.processProblemSubmissionEvent(evt)
			default:
				c.log.Error().Msg("Unknown event type, skipping...")
			}

		}(event)
	}

	wg.Wait()
	return nil
}

// processSubmissionEvent processes the SubmissionDTO type event
func (c *Controller[T]) processSubmissionEvent(subEvent broker.SubmissionDTO) {
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
func (c *Controller[T]) processProblemSubmissionEvent(problemEvent broker.ProblemSubmissionKafkaDTO) {
	c.log.Info().Msg("Started processing ProblemSubmissionKafkaDTO event")

	// Process submission part of the event
	c.processSubmissionEvent(problemEvent.SubmissionDTO)

	// Process each test case
	for _, testCase := range problemEvent.TestCases {
		c.processTestCase(testCase, problemEvent.SubmissionDTO)
	}
}

// processTestCase handles the execution of test cases
func (c *Controller[T]) processTestCase(testCase broker.TestCaseDTO, subEvent broker.SubmissionDTO) {
	c.log.Info().Msg("Processing test case: " + testCase.ID.String())

	exec, err := codeexec.NewDockerExecutor(subEvent.Language)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to create code executor for test case")
		return
	}

	// Execute the test case using the test input
	output, err := exec.ExecuteTest(subEvent.Code, testCase.Input)
	if err != nil {
		c.log.Error().Err(err).Msg("Test case execution failed")
		return
	}

	// Log the result of the test case
	c.log.Info().Msg("Test case " + testCase.ID.String() + " output: " + output)
}
