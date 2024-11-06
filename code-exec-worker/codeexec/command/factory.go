package command

import (
	"fmt"

	"github.com/St3pegor/jcode/broker"
)

// CommandGeneratorFactory creates instances of CommandGenerator.
type CommandGeneratorFactory struct{}

func (f *CommandGeneratorFactory) CreateGenerator(lang broker.Language) (CommandGenerator, error) {
    switch lang {
    case broker.PYTHON:
        return &PythonCommandGenerator{}, nil
    case broker.JAVA:
        return &JavaCommandGenerator{}, nil
    case broker.GO:
        return &GoCommandGenerator{}, nil
    default:
        return nil, fmt.Errorf("unsupported language: %s", lang)
    }
}
