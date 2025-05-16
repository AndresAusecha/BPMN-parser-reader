package com.camunda.bpmn.clients;

import com.camunda.bpmn.DTOs.XmlFetchResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class XmlClient {
    static String XML_URL = "https://n35ro2ic4d.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/process-definition/key/invoice/xml";

    public static String fetchXml() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(XML_URL))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            XmlFetchResult result = objectMapper.readValue(response.body(), XmlFetchResult.class);

            return result.getBpmn20Xml();
        } catch (UncheckedIOException | IOException | InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        return "";
    }
}
