/*
 * Copyright 2024 PixelsDB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pixelsdb.pixels.rover.controller;

import io.pixelsdb.pixels.rover.constant.RestUrlPath;
import io.pixelsdb.pixels.rover.rest.request.TextToSQLRequest;
import io.pixelsdb.pixels.rover.rest.response.TextToSQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
public class TextToSQLController
{
    private final WebClient webClient;

    @Autowired
    public TextToSQLController(WebClient.Builder webClientBuilder)
    {
        String BASE_URL = "http://10.77.110.127:10000";
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @PostMapping(value = RestUrlPath.Text_to_SQL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TextToSQLResponse getSqlFromText(@RequestBody TextToSQLRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri("/get")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), TextToSQLResponse.class)
                    .retrieve()
                    .bodyToMono(TextToSQLResponse.class)
                    .block(); // block to wait for the response;
        } catch (Exception e)
        {
            System.out.println("error");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
