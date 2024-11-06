package command

import "fmt"

// GoCommandGenerator generates commands for Go code.
type GoCommandGenerator struct{}

func (g *GoCommandGenerator) GenerateCommand(code string) ([]string, error) {
    return []string{"sh", "-c", fmt.Sprintf("echo '%s' > main.go && go run main.go", code)}, nil
}
