package command

import "fmt"

// PythonCommandGenerator generates commands for Python code.
type PythonCommandGenerator struct{}

func (p *PythonCommandGenerator) GenerateCommand(code string) ([]string, error) {
    return []string{"python3", "-c", fmt.Sprintf(`%s`, code)}, nil
}
