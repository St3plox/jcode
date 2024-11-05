package producer

type Producer interface {
	ProduceEvents(string, []any) error
}