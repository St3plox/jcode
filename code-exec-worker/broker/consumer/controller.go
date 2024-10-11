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

	// Process submission part of the event
	// c.processSubmissionEvent(problemEvent.SubmissionDTO)

	// // Process each test case
	// for _, testCase := range problemEvent.TestCases {
	// 	c.processTestCase(testCase, problemEvent.SubmissionDTO)
	// }
}

// processTestCase handles the execution of test cases
func (c *Controller) processTestCase(testCase broker.TestCaseDTO, subEvent broker.SubmissionDTO) {
	c.log.Info().Msg("Processing test case: " + testCase.ID)

	// exec, err := codeexec.NewDockerExecutor(subEvent.Language)
	// if err != nil {
	// 	c.log.Error().Err(err).Msg("Failed to create code executor for test case")
	// 	return
	// }

	// // Execute the test case using the test input
	// // output, err := exec.ExecuteTest(subEvent.Code, testCase.Input)
	// // if err != nil {
	// // 	c.log.Error().Err(err).Msg("Test case execution failed")
	// // 	return
	// // }

	// Log the result of the test case
	// c.log.Info().Msg("Test case " + testCase.ID.String() + " output: " + output)
}
