package command

// CommandGenerator is an interface for generating commands for
type CommandGenerator interface {
    GenerateCommand(code string) ([]string, error)
}