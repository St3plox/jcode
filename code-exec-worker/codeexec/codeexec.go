package codeexec

import (
	"bytes"
	"fmt"
	"log"

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
