package com.camunda.bpmn.exceptions;

public class BPMNParsingError extends Exception {
    public BPMNParsingError(String errorMessage) {
        super(errorMessage);
    }
}
