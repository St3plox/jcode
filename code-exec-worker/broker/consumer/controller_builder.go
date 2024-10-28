package consumer

import (
	"fmt"
	"os"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/rs/zerolog"
)

// ControllerBuilder helps build a Controller with optional fields.
type ControllerBuilder struct {
	submissionConsumer        *Consumer[broker.SubmissionDTO]
	problemSubmissionConsumer *Consumer[broker.ProblemSubmissionKafkaDTO]
	producer                  *producer.Producer
	delay                     *time.Duration
	log                       *zerolog.Logger
	shutdown                  chan os.Signal
}

// NewControllerBuilder initializes the builder with default values.
func NewControllerBuilder() *ControllerBuilder {
	return &ControllerBuilder{}
}

// WithSubmissionConsumer sets the submissionConsumer.
func (b *ControllerBuilder) WithSubmissionConsumer(c *Consumer[broker.SubmissionDTO]) *ControllerBuilder {
	b.submissionConsumer = c
	return b
}

// WithProblemSubmissionConsumer sets the problemSubmissionConsumer.
func (b *ControllerBuilder) WithProblemSubmissionConsumer(c *Consumer[broker.ProblemSubmissionKafkaDTO]) *ControllerBuilder {
	b.problemSubmissionConsumer = c
	return b
}

// WithProducer sets the producer.
func (b *ControllerBuilder) WithProducer(p producer.Producer) *ControllerBuilder {
	b.producer = &p
	return b
}

func (b *ControllerBuilder) WithDelay(d time.Duration) *ControllerBuilder {
	b.delay = &d
	return b
}

func (b *ControllerBuilder) WithLogger(l *zerolog.Logger) *ControllerBuilder {
	b.log = l
	return b
}

func (b *ControllerBuilder) WithShutdown(s chan os.Signal) *ControllerBuilder {
	b.shutdown = s
	return b
}

func (b *ControllerBuilder) Build() (*Controller, error) {
	// Validation: Ensure mandatory fields are set.
	if b.submissionConsumer == nil {
		return nil, fmt.Errorf("submissionConsumer is required")
	}
	// if b.problemSubmissionConsumer == nil {
	// 	return nil, fmt.Errorf("problemSubmissionConsumer is required")
	// }
	if b.producer == nil {
		return nil, fmt.Errorf("producer is required")
	}
	if b.delay == nil {
		return nil, fmt.Errorf("delay is required")
	}
	if b.log == nil {
		return nil, fmt.Errorf("log is required")
	}
	if b.shutdown == nil {
		return nil, fmt.Errorf("shutdown is required")
	}

	return &Controller{
		submissionConsumer:        *b.submissionConsumer,
		problemSubmissionConsumer: *b.problemSubmissionConsumer,
		producer:                  *b.producer,
		delay:                     *b.delay,
		log:                       b.log,
		shutdown:                  b.shutdown,
	}, nil
}
