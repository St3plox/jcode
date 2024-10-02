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

type submissionConsumer interface {
	Consume(ctx context.Context) (<-chan broker.SubmissionDTO, error)
}

// Controller defines the structure that manages event consumption and code execution
type Controller struct {
	consumer submissionConsumer
	producer producer.Producer
	delay    time.Duration
	log      *zerolog.Logger
	shutdown chan os.Signal
}

// New creates a new Controller
func New(consumer submissionConsumer, producer producer.Producer, delay time.Duration, log *zerolog.Logger, shutdown chan os.Signal) *Controller {
	return &Controller{
		consumer: consumer,
		producer: producer,
		delay:    delay,
		log:      log,
		shutdown: shutdown,
	}
}

// ConcurrentListenForEvents listens to events from Kafka and processes each event concurrently
func (c *Controller) ConcurrentListenForEvents(ctx context.Context) error {
	submissionEventChannel, err := c.consumer.Consume(ctx)
	if err != nil {
		c.log.Error().Err(err).Msg("Failed to start consuming events")
		return err
	}

	var wg sync.WaitGroup

	for subEvent := range submissionEventChannel {
		wg.Add(1)

		go func(subEvent broker.SubmissionDTO) {
			defer wg.Done()

			c.log.Info().Msg("Started processing submission event")

			exec, err := codeexec.NewDockerExecutor(subEvent.Language)
			if err != nil {
				c.log.Error().Err(err).Msg("Failed to create code executor")
				c.producer.ProduceResultEvents([]broker.ResultDTO{broker.MapTo(subEvent, "", err.Error())})
				return
			}

			output, err := exec.Execute(subEvent.Code)
			if err != nil {
				c.log.Error().Err(err).Msg("Failed to execute code")
				c.producer.ProduceResultEvents([]broker.ResultDTO{broker.MapTo(subEvent, output, err.Error())})
				return
			}

			c.producer.ProduceResultEvents([]broker.ResultDTO{broker.MapTo(subEvent, output, "")})
			c.log.Info().Msg(subEvent.ID.String() + " processed \n output: " + output)
		}(subEvent) 
	}

	wg.Wait()

	return nil
}
