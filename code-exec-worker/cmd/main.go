package main

import (
	"context"
	"fmt"
	"os"
	"runtime"
	"time"

	"github.com/St3pegor/jcode/broker/consumer"
	"github.com/St3plox/Blogchain/foundation/logger"
	"github.com/ardanlabs/conf/v3"
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
	// initializing consumer support

	kafkaAdress := os.Getenv("KAFKA_ADDRESS")
	if kafkaAdress == "" {
		kafkaAdress = cfg.SubmissionConsumer.Address
	}

	//TODO: remove to cfcg
	subConsumer, err := consumer.NewSubmissionConsumer(
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

	subController := consumer.New(subConsumer, time.Second, log)

	serverErrors := make(chan error, 1)
	go func() {
		log.Info().
			Str("status", "service started").
			Msg("startup")
		serverErrors <- subController.ListenForEvents(context.Background())
	}()

	// -------------------------------------------------------------------------
	// Shutdown

	select {
	case err := <-serverErrors:
		return fmt.Errorf("server error: %w", err)

		// case sig := <-shutdown:
		// 	log.Info().
		// 		Str("status", "shutdown started").
		// 		Str("signal", sig.String()).
		// 		Msg("shutdown")
		// 	defer log.Info().Str("status", "shutdown complete").
		// 		Str("signal", sig.String()).
		// 		Msg("shutdown")

		// 	ctx, cancel := context.WithTimeout(context.Background(), cfg.Web.ShutdownTimeout)
		// 	defer cancel()

	}

	// return nil
}
