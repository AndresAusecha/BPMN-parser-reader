package com.camunda.bpmn.parsers;

import com.camunda.bpmn.exceptions.BPMNParsingError;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XmlParser {
    public static BpmnModelInstance readFromText(String textString) throws BPMNParsingError {
        if (textString == null || textString.isEmpty()) {
            throw new BPMNParsingError("the XML provided is invalid");
        }

        InputStream inputStream = new ByteArrayInputStream(textString.getBytes(StandardCharsets.UTF_8));

        return Bpmn.readModelFromStream(inputStream);
    }
}
