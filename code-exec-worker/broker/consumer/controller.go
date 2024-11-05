package consumer

import (
	"context"
	"os"
	"sync"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/service"
	"github.com/rs/zerolog"
)

type Controller struct {
	submissionConsumer        Consumer[broker.SubmissionDTO]
	problemSubmissionConsumer Consumer[broker.ProblemSubmissionKafkaDTO]
	submissionProcessor       service.Processor
	problemProcessor          service.Processor
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
		c.submissionProcessor.Process(subEvent)
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
		c.problemProcessor.Process(probEvent)
	}
	return nil
}
