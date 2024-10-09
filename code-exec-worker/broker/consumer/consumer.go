package consumer

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/rs/zerolog"
)

type Consumer[T any] struct {
	consumer      *kafka.Consumer
	topic         string
	log           *zerolog.Logger
	channelBuffer int
	retryDelay    time.Duration
}

func NewConsumer[T any](adrr, groupID, topic string, log *zerolog.Logger, bufferSize int, retryDelay time.Duration) (*Consumer[T], error) {
	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": adrr,
		"group.id":          groupID,
		"auto.offset.reset": "earliest",
	})
	if err != nil {
		return nil, err
	}

	return &Consumer[T]{
		consumer:      consumer,
		topic:         topic,
		log:           log,
		channelBuffer: bufferSize,
		retryDelay:    retryDelay,
	}, nil
}

func (c *Consumer[T]) Consume(ctx context.Context) (<-chan T, error) {
	if err := c.consumer.SubscribeTopics([]string{c.topic}, nil); err != nil {
		return nil, err
	}

	ch := make(chan T, c.channelBuffer)
	var wg sync.WaitGroup

	wg.Add(1)
	go func() {
		defer wg.Done()
		retryDelay := c.retryDelay

		for {
			select {
			case <-ctx.Done():
				c.log.Info().Msg("Shutting down consumer...")
				c.consumer.Close()
				close(ch)
				return

			default:
				msg, err := c.consumer.ReadMessage(-1)
				if err != nil {
					c.log.Error().Err(err).Msg("Error reading message, retrying with backoff")
					time.Sleep(retryDelay)
					retryDelay *= 2
					if retryDelay > 30*time.Second {
						retryDelay = 30 * time.Second
					}
					continue
				}

				retryDelay = c.retryDelay

				var event T
				if err := json.Unmarshal(msg.Value, &event); err != nil {
					c.log.Error().Err(err).Msg("Error unmarshalling Kafka message")
					continue
				}

				select {
				case ch <- event:
				case <-ctx.Done():
					c.log.Info().Msg("Context cancelled while sending event")
					return
				}
			}
		}
	}()

	go func() {
		<-ctx.Done()
		wg.Wait()
		c.log.Info().Msg("Consumer shutdown complete")
	}()

	return ch, nil
}
