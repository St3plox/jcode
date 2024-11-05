package main

import (
	"context"
	"fmt"
	"os"
	"runtime"
	"time"

	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/broker/consumer"
	"github.com/St3pegor/jcode/broker/producer"
	"github.com/St3pegor/jcode/service"
	"github.com/St3plox/Blogchain/foundation/logger"
	"github.com/ardanlabs/conf/v3"
	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/rs/zerolog"
)

var build = "develop"

func main() {
	log := logger.New("CODE-EXEC-WORKER")

	if err := run(log); err != nil {
		log.Error().Err(err).Msg("startup")
		os.Exit(1)
	}
}

func run(log *zerolog.Logger) error {

	// -------------------------------------------------------------------------
	// GOMAXPROCS
	log.Info().Str("startup", "GOMAXPROCS").Int("GOMAXPROCS", runtime.
		GOMAXPROCS(0)).
		Str("BUILD", build).
		Msg("startup")

	// -------------------------------------------------------------------------
	// Configuration

	cfg := struct {
		conf.Version
		Web struct {
			ReadTimeout     time.Duration `conf:"default:5s"`
			WriteTimeout    time.Duration `conf:"default:10s"`
			IdleTimeout     time.Duration `conf:"default:120s"`
			ShutdownTimeout time.Duration `conf:"default:20s,mask"`
			APIHost         string        `conf:"default::3000"`
			DebugHost       string        `conf:"default::4000"`
		}
		SubmissionConsumer struct {
			RetryDelay time.Duration `conf:"default:5s"`
			Topic      string        `conf:"default:submissions"`
			Address    string        `conf:"default:localhost:9092"`
			Group      string        `conf:"default:jcode-group"`
			BufferSize int           `conf:"default:8"`
		}
	}{
		Version: conf.Version{
			Build: build,
			Desc:  "copyright information here",
		},
	}

	fmt.Println(cfg)

	// -------------------------------------------------------------------------
	// App Starting

	log.Info().Str("version", build).Msg("starting service")
	defer log.Info().Msg("shutdown complete")

	// -------------------------------------------------------------------------
	// initializing producer support
	kafkaProducer, err := kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": "localhost"})
	if err != nil {
		return err
	}

	resultProducer := producer.NewResultProducer(kafkaProducer)

	// -------------------------------------------------------------------------
	// initializing consumer support

	kafkaAdress := os.Getenv("KAFKA_ADDRESS")
	if kafkaAdress == "" {
		kafkaAdress = cfg.SubmissionConsumer.Address
	}

	//TODO: remove to cfg
	subConsumer, err := consumer.NewConsumer[broker.SubmissionDTO](
		"localhost:9092",
		"jcode-group",
		"submissions",
		log,
		runtime.GOMAXPROCS(0),
		time.Microsecond*10,
	)
	if err != nil {
		return err
	}

	probSubConsuner, err := consumer.NewConsumer[broker.ProblemSubmissionKafkaDTO](
		"localhost:9092",
		"jcode-group",
		"problem_submissions",
		log,
		runtime.GOMAXPROCS(0),
		time.Microsecond*10,
	)
	if err != nil {
		return err
	}

	shutdown := make(chan os.Signal, 1)

	submissionProcessor := service.NewSubmissionProcessor(resultProducer, log)
	problemProcessor := service.NewProblemSubmissionProcessor(resultProducer, log)

	controller, err := consumer.NewControllerBuilder().
		WithLogger(log).
		WithSubmissionConsumer(subConsumer).
		WithProblemSubmissionConsumer(probSubConsuner).
		WithSubmissionProcessor(submissionProcessor).
		WithProblemProcessor(problemProcessor).
		WithShutdown(shutdown).
		Build()
	if err != nil {
		return err
	}

	serverErrors := make(chan error, 1)
	go func() {
		log.Info().
			Str("status", "service started").
			Msg("startup")
		serverErrors <- controller.ListenForEvents(context.Background())
	}()

	// -------------------------------------------------------------------------
	// Shutdown

	select {
	case err := <-serverErrors:
		return fmt.Errorf("server error: %w", err)

	case sig := <-shutdown:
		log.Info().
			Str("status", "shutdown started").
			Str("signal", sig.String()).
			Msg("shutdown")
		defer log.Info().Str("status", "shutdown complete").
			Str("signal", sig.String()).
			Msg("shutdown")
		_, cancel := context.WithTimeout(context.Background(), cfg.Web.ShutdownTimeout)
		defer cancel()
		return fmt.Errorf("Shutdown")

	}
}
