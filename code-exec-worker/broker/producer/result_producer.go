package producer

import (
	"encoding/json"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

var topic = "result"

type Producer interface {
	ProduceResultEvents(likesEvents []ResultDTO) error
}

type ResultProducer struct {
	producer *kafka.Producer
}

func NewResultProducer(producer *kafka.Producer) *ResultProducer {
	return &ResultProducer{producer}
}

func (lp *ResultProducer) ProduceResultEvents(resultEvents []ResultDTO) error {
	for _, resultEvent := range resultEvents {

		encodedEvent, err := json.Marshal(resultEvent)
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
