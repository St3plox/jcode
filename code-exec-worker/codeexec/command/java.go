package command

import (
    "fmt"
    "regexp"
)

// JavaCommandGenerator generates commands for Java code.
type JavaCommandGenerator struct{}

func (j *JavaCommandGenerator) GenerateCommand(code string) ([]string, error) {
    className, err := extractJavaClassName(code)
    if err != nil {
        return nil, fmt.Errorf("failed to extract Java class name: %w", err)
    }
    return []string{"sh", "-c", fmt.Sprintf("echo '%s' > %s.java && javac %s.java && java %s", code, className, className, className)}, nil
}

func extractJavaClassName(code string) (string, error) {
    re := regexp.MustCompile(`(?m)public\s+class\s+(\w+)`)
    matches := re.FindStringSubmatch(code)
    if len(matches) < 2 {
        return "", fmt.Errorf("no public class found in Java code")
    }
    return matches[1], nil
}
