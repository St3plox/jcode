package codeexec

import (
	"github.com/St3pegor/jcode/broker"
	"github.com/St3pegor/jcode/codeexec/command"
)

// genCmd generates cmd commands to launch code on a specific language
func genCmd(code string, lang broker.Language) ([]string, error) {
    factory := &command.CommandGeneratorFactory{}
    generator, err := factory.CreateGenerator(lang)
    if err != nil {
        return nil, err
    }
    return generator.GenerateCommand(code)
}