package producer

type Producer interface {
	ProduceEvents([]any) error
}