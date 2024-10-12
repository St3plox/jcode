package codeexec

import (
	"fmt"
	"regexp"

	"github.com/St3pegor/jcode/broker"
)

// genCmd generates cmd commands to launch code on a specific language
func genCmd(code string, lang broker.Language) ([]string, error) {

	var cmd []string
	switch lang {
	case broker.PYTHON:
        return []string{"python3", "-c", fmt.Sprintf(`%s`, code)}, nil

	case broker.JAVA:
		className, err := extractJavaClassName(code)
		if err != nil {
			return nil, fmt.Errorf("failed to extract Java class name: %w", err)
		}

		cmd = []string{"sh", "-c", fmt.Sprintf("echo '%s' > %s.java && javac %s.java && java %s", code, className, className, className)}

	case broker.GO:
		cmd = []string{"sh", "-c", fmt.Sprintf("echo '%s' > main.go && go run main.go", code)}

	default:
		return nil, fmt.Errorf("unsupported language: %s", lang)
	}

	return cmd, nil
}

// extractJavaClassName extracts the public class name from the Java code.
// It looks for the "public class <ClassName>" pattern.
func extractJavaClassName(code string) (string, error) {
	re := regexp.MustCompile(`(?m)public\s+class\s+(\w+)`)
	matches := re.FindStringSubmatch(code)
	if len(matches) < 2 {
		return "", fmt.Errorf("no public class found in Java code")
	}
	return matches[1], nil
}
