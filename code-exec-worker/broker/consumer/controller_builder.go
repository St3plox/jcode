package consumer

import (
    "fmt"
    "os"

    "github.com/St3pegor/jcode/broker"
    "github.com/St3pegor/jcode/service"
    "github.com/rs/zerolog"
)

// ControllerBuilder helps build a Controller with optional fields.
type ControllerBuilder struct {
    submissionConsumer        *Consumer[broker.SubmissionDTO]
    problemSubmissionConsumer *Consumer[broker.ProblemSubmissionKafkaDTO]
    submissionProcessor       *service.Processor
    problemProcessor          *service.Processor
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

// WithSubmissionProcessor sets the submissionProcessor.
func (b *ControllerBuilder) WithSubmissionProcessor(p service.Processor) *ControllerBuilder {
    b.submissionProcessor = &p
    return b
}

// WithProblemProcessor sets the problemProcessor.
func (b *ControllerBuilder) WithProblemProcessor(p service.Processor) *ControllerBuilder {
    b.problemProcessor = &p
    return b
}

// WithLogger sets the logger.
func (b *ControllerBuilder) WithLogger(l *zerolog.Logger) *ControllerBuilder {
    b.log = l
    return b
}

// WithShutdown sets the shutdown channel.
func (b *ControllerBuilder) WithShutdown(s chan os.Signal) *ControllerBuilder {
    b.shutdown = s
    return b
}

// Build constructs the Controller with the set fields, validating required fields.
func (b *ControllerBuilder) Build() (*Controller, error) {
    // Validation: Ensure mandatory fields are set.
    if b.submissionConsumer == nil {
        return nil, fmt.Errorf("submissionConsumer is required")
    }
    if b.problemSubmissionConsumer == nil {
        return nil, fmt.Errorf("problemSubmissionConsumer is required")
    }
    if b.submissionProcessor == nil {
        return nil, fmt.Errorf("submissionProcessor is required")
    }
    if b.problemProcessor == nil {
        return nil, fmt.Errorf("problemProcessor is required")
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
        submissionProcessor:       *b.submissionProcessor,
        problemProcessor:          *b.problemProcessor,
        log:                       b.log,
        shutdown:                  b.shutdown,
    }, nil
}
