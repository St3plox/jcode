package codeexec

import (
	"errors"
	"fmt"

	"github.com/St3pegor/jcode/broker"
)

func dockerImage(lang broker.Language) (string, error) {
	switch lang {
	case broker.GO:
		return "golang:latest", nil
	case broker.JAVA:
		return "openjdk:latest", nil
	case broker.PYTHON:
		return "python:3.9-slim", nil
	default:
		return "", errors.New(fmt.Sprintf("unsupported language: %s", lang))
	}
}
