package io.pixelsdb.pixels.rover.controller;

import io.pixelsdb.pixels.common.server.rest.request.GetSchemasRequest;
import io.pixelsdb.pixels.common.server.rest.response.GetSchemasResponse;
import io.pixelsdb.pixels.rover.constant.RestUrlPath;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import io.pixelsdb.pixels.rover.rest.request.TextToSQLRequest;
import io.pixelsdb.pixels.rover.rest.response.TextToSQLResponse;
import io.pixelsdb.pixels.rover.constant.RestUrlPath;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TextToSQLController {
    private final WebClient webClient;

    @Autowired
    public TextToSQLController(WebClient.Builder webClientBuilder) {
        String BASE_URL = "http://10.77.110.127:10000";
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @PostMapping(value = RestUrlPath.Text_to_SQL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TextToSQLResponse getSqlFromText(@RequestBody TextToSQLRequest request) {
        try {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri("/get")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), TextToSQLResponse.class)
                    .retrieve()
                    .bodyToMono(TextToSQLResponse.class)
                    .block(); // block to wait for the response;
        } catch (Exception e) {
            System.out.println("error");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
