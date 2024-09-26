package consumer

import (
	"context"
	"os"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/St3pegor/jcode/codeexec"
	"github.com/rs/zerolog"
)

type submissionConsumer interface {
	Consume(ctx context.Context) (<-chan broker.SubmissionDTO, error)
}

// Controller defines the structure that manages event consumption and email sending
type Controller struct {
	consumer submissionConsumer
	producer producer.Producer
	delay    time.Duration
	log      *zerolog.Logger
	shutdown chan os.Signal
}

// New creates a new Controller
// TODO: Replace with cfg
func New(consumer submissionConsumer, producer producer.Producer, delay time.Duration, log *zerolog.Logger, shutdown chan os.Signal) *Controller {
	return &Controller{
		consumer: consumer,
		producer: producer,
		delay:    delay,
		log:      log,
		shutdown: shutdown,
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
			c.log.Info().Msg("Started processing submission event")

			exec, err := codeexec.NewDockerExecutor(subEvent.Language)
			if err != nil {
				c.log.Error().Err(err).Msg("failed to create code executor")
				c.producer.ProduceResultEvents([]producer.ResultDTO{producer.MapTo(subEvent, "", err.Error())})
				return err
			}

			output, err := exec.Execute(subEvent.Code)
			if err != nil {
				c.log.Error().Err(err).Msg("Failed to execute code")
				c.producer.ProduceResultEvents([]producer.ResultDTO{producer.MapTo(subEvent, output, err.Error())})
				return err
			}

			c.producer.ProduceResultEvents([]producer.ResultDTO{producer.MapTo(subEvent, output, "")})
			c.log.Info().Msg(subEvent.ID.String() + " processed \n output: " + output)
		}
	}
}
