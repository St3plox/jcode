package consumer

import (
	"context"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/rs/zerolog"
)

type likeConsumer interface {
	Consume(ctx context.Context) (<-chan broker.SubmissionDTO, error)
}

// Controller defines the structure that manages event consumption and email sending
type Controller struct {
	consumer likeConsumer
	delay    time.Duration
	log      *zerolog.Logger
}

// New creates a new Controller
func New(consumer likeConsumer, delay time.Duration, log *zerolog.Logger) *Controller {
	return &Controller{
		consumer: consumer,
		delay:    delay,
		log:      log,
	}
}

// ListenForEvents listens to like events from Kafka and sends emails for each event
func (c *Controller) ListenForEvents(ctx context.Context) error {
	for {
		submisionEventChannel, err := c.consumer.Consume(ctx)
		if err != nil {
			c.log.Error().Err(err).Msg("Failed to start consuming events")
			return err
		}

		for subEvent := range submisionEventChannel {
			c.log.Info().Msg("Started processing submisiion event")

			exec, err := codeexec.NewDockerExecutor(subEvent.Language)
			if err != nil {
				c.log.Error().Err(err).Msg("failed to create code executor")
				return err
			}

			output, err := exec.Execute(subEvent.Code)
			if err != nil {
				c.log.Error().Err(err).Msg("Failed to execute code")
				return err
			}

			c.log.Info().Msg(subEvent.ID.String() + " processed \n output: " + output)
		}
	}
}
