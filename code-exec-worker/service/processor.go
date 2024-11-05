package service

type Processor interface {
	Process(event interface{})
}
