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

import io.pixelsdb.pixels.common.server.rest.request.GetQueryResultRequest;
import io.pixelsdb.pixels.common.server.rest.request.GetQueryStatusRequest;
import io.pixelsdb.pixels.common.server.rest.request.SubmitQueryRequest;
import io.pixelsdb.pixels.common.server.rest.response.GetQueryResultResponse;
import io.pixelsdb.pixels.common.server.rest.response.GetQueryStatusResponse;
import io.pixelsdb.pixels.common.server.rest.response.SubmitQueryResponse;
import io.pixelsdb.pixels.common.utils.ConfigFactory;
import io.pixelsdb.pixels.rover.constant.RestUrlPath;
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
public class QueryController
{
    private final WebClient webClient;

    @Autowired
    public QueryController(WebClient.Builder webClientBuilder)
    {
        String host = ConfigFactory.Instance().getProperty("metadata.server.host");
        assert (host != null);
        int port = 18890;
        String BASE_URL = "http://" + host + ":" + port;
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @PostMapping(value = RestUrlPath.SUBMIT_QUERY,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmitQueryResponse submitQuery(@RequestBody SubmitQueryRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.SUBMIT_QUERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), SubmitQueryRequest.class)
                    .retrieve()
                    .bodyToMono(SubmitQueryResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = RestUrlPath.GET_QUERY_STATUS,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetQueryStatusResponse getQueryStatus(@RequestBody GetQueryStatusRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_QUERY_STATUS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetQueryStatusRequest.class)
                    .retrieve()
                    .bodyToMono(GetQueryStatusResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = RestUrlPath.GET_QUERY_RESULT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetQueryResultResponse getQueryResult(@RequestBody GetQueryResultRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_QUERY_RESULT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetQueryResultRequest.class)
                    .retrieve()
                    .bodyToMono(GetQueryResultResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}