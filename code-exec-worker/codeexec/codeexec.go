package codeexec

import (
	"bytes"
	"fmt"
	"log"
	"time"

	"github.com/St3pegor/jcode/broker"
	docker "github.com/fsouza/go-dockerclient"
)

// Executor defines the interface for executing code.
type Executor interface {
	Execute(code string) (string, error)
}

// DockerExecutor implements the Executor interface to execute code in Docker.
type DockerExecutor struct {
	client *docker.Client
	lang   broker.Language
}

// NewDockerExecutor creates a new DockerExecutor with the specified image.
func NewDockerExecutor(lang broker.Language) (*DockerExecutor, error) {

	client, err := docker.NewClientFromEnv()
	if err != nil {
		return nil, fmt.Errorf("failed to create Docker client: %w", err)
	}

	return &DockerExecutor{client: client, lang: lang}, nil
}

// Execute runs the provided code in a Docker container and returns the output.
// The command used for execution is determined by the language.
func (de *DockerExecutor) Execute(code string) (string, error) {

	img, err := dockerImage(de.lang)
	if err != nil {
		return "", fmt.Errorf("failed to parse language: %w", err)
	}

	if err := de.pullImage(img); err != nil {
		return "", err
	}

	cmd, err := genCmd(code, de.lang)
	if err != nil {
		return "", err
	}

	// Create a container
	container, err := de.client.CreateContainer(docker.CreateContainerOptions{
		Config: &docker.Config{
			Image:        img,
			Cmd:          cmd,
			AttachStdout: true,
			AttachStderr: true,
		},
	})
	if err != nil {
		return "", fmt.Errorf("failed to create Docker container: %w", err)
	}
	defer de.cleanup(container.ID)

	// Start the container
	if err := de.client.StartContainer(container.ID, nil); err != nil {
		return "", fmt.Errorf("failed to start Docker container: %w", err)
	}

	// Wait for the container to finish execution
	_, err = de.client.WaitContainer(container.ID)
	if err != nil {
		return "", fmt.Errorf("error while waiting for container: %w", err)
	}

	// Fetch logs after container execution
	output, err := de.getLogs(container.ID)
	if err != nil {
		return "", err
	}

	return output, nil
}

// Execute runs the provided code in a Docker container and returns the output.
// The command used for execution is determined by the language.
// 'inputs' is a slice of inputs passed to the code, and it returns the actual output.
func (de *DockerExecutor) ExecuteWithInputs(code string, input string) (string, error) {

	img, err := dockerImage(de.lang)
	if err != nil {
		return "", fmt.Errorf("failed to parse language: %w", err)
	}

	if err := de.pullImage(img); err != nil {
		return "", fmt.Errorf("failed to pull Docker image: %w", err)
	}

	cmd, err := genCmd(code, de.lang)
	if err != nil {
		return "", fmt.Errorf("failed to generate command: %w", err)
	}

	// Create container with stdin open
	container, err := de.client.CreateContainer(docker.CreateContainerOptions{
		Config: &docker.Config{
			Image:        img,
			Cmd:          cmd,
			AttachStdout: true,
			AttachStderr: true,
			AttachStdin:  true,
			OpenStdin:    true,
			StdinOnce:    true,
		},
	})
	if err != nil {
		return "", fmt.Errorf("failed to create Docker container: %w", err)
	}
	defer de.cleanup(container.ID)

	if err := de.client.StartContainer(container.ID, nil); err != nil {
		return "", fmt.Errorf("failed to start Docker container: %w", err)
	}

	// Send input if provided
	if len(input) > 0 {
		if err := de.sendInputDirectly(container.ID, input); err != nil {
			return "", fmt.Errorf("failed to send input to container: %w", err)
		}
	}


	if err := de.waitForContainer(container.ID, 60*time.Second); err != nil {
		return "", fmt.Errorf("error waiting for container: %w", err)
	}

	output, err := de.getLogs(container.ID)
	if err != nil {
		return "", err
	}

	return output, nil
}

func (de *DockerExecutor) sendInputDirectly(containerID, input string) error {

	// Attach to the container's stdin, stdout, and stderr
	inputBuffer := bytes.NewBufferString(input)

	opts := docker.AttachToContainerOptions{
		Container:   containerID,
		InputStream: inputBuffer,
		Stdin:       true,
		Stream:      true,
		Stdout:      false,
		Stderr:      false,
	}

	// Attach input stream to container
	err := de.client.AttachToContainer(opts)
	if err != nil {
		return fmt.Errorf("failed to send input to container: %w", err)
	}

	return nil
}

func (de *DockerExecutor) sendInputToContainer(containerID, input string) error {

	exec, err := de.client.CreateExec(docker.CreateExecOptions{
		Container:    containerID,
		Cmd:          []string{"sh", "-c", fmt.Sprintf("echo -n '%s' | cat", input)},
		AttachStdin:  true,
		AttachStdout: true,
	})

	if err != nil {
		return fmt.Errorf("failed to create exec instance for container: %w", err)
	}

	err = de.client.StartExec(exec.ID, docker.StartExecOptions{
		InputStream: bytes.NewBufferString(input),
	})
	if err != nil {
		return fmt.Errorf("failed to send input to container: %w", err)
	}

	return nil
}

// pullImage pulls the Docker image if not already present.
func (de *DockerExecutor) pullImage(img string) error {

	err := de.client.PullImage(docker.PullImageOptions{
		Repository: img,
	}, docker.AuthConfiguration{})

	if err != nil {
		return fmt.Errorf("failed to pull Docker image: %w", err)
	}
	return nil
}

// waitForContainer waits for the container to finish execution, with a timeout.
func (de *DockerExecutor) waitForContainer(containerID string, timeout time.Duration) error {
	done := make(chan error, 1)

	go func() {
		_, err := de.client.WaitContainer(containerID) // Block until container finishes
		done <- err
	}()

	select {
	case err := <-done:
		if err != nil {
			return fmt.Errorf("container %s exited with error: %w", containerID, err)
		}
		return nil
	case <-time.After(timeout):
		return fmt.Errorf("timeout waiting for container %s to finish", containerID)
	}
}

// getLogs fetches the logs from the container.
func (de *DockerExecutor) getLogs(containerID string) (string, error) {

	var stdout, stderr bytes.Buffer

	err := de.client.Logs(docker.LogsOptions{
		Container:    containerID,
		OutputStream: &stdout,
		ErrorStream:  &stderr,
		Stdout:       true,
		Stderr:       true,
		Follow:       true,
	})
	if err != nil {
		return "", fmt.Errorf("error fetching logs: %w", err)
	}

	// Check if there were any errors
	if stderr.Len() > 0 {
		return stderr.String(), fmt.Errorf("error output from container: %s", stderr.String())
	}

	return stdout.String(), nil
}

// cleanup removes the container after execution.
func (de *DockerExecutor) cleanup(containerID string) {

	err := de.client.RemoveContainer(docker.RemoveContainerOptions{ID: containerID, Force: true})
	if err != nil {
		log.Printf("Error during cleanup of container %s: %v", containerID, err)
	}
}
