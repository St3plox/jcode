package producer

import (
	"encoding/json"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

var topic = "problem"

type ResultProducer struct {
	producer *kafka.Producer
}

func NewResultProducer(producer *kafka.Producer) *ResultProducer {
	return &ResultProducer{producer}
}

func (lp *ResultProducer) ProduceEvents(events []any) error {
	for _, event := range events {

		encodedEvent, err := json.Marshal(event)
		if err != nil {
			return err
		}

		message := &kafka.Message{
			TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
			Value:          encodedEvent,
		}

		if err := lp.producer.Produce(message, nil); err != nil {
			return err
		}
	}
	return nil
}
