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

import io.pixelsdb.pixels.common.server.rest.request.GetColumnsRequest;
import io.pixelsdb.pixels.common.server.rest.request.GetSchemasRequest;
import io.pixelsdb.pixels.common.server.rest.request.GetTablesRequest;
import io.pixelsdb.pixels.common.server.rest.request.GetViewsRequest;
import io.pixelsdb.pixels.common.server.rest.response.GetColumnsResponse;
import io.pixelsdb.pixels.common.server.rest.response.GetSchemasResponse;
import io.pixelsdb.pixels.common.server.rest.response.GetTablesResponse;
import io.pixelsdb.pixels.common.server.rest.response.GetViewsResponse;
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
public class MetadataController
{
    private final WebClient webClient;

    @Autowired
    public MetadataController(WebClient.Builder webClientBuilder)
    {
        String host = ConfigFactory.Instance().getProperty("metadata.server.host");
        assert (host != null);
        int port = 18890;
        String BASE_URL = "http://" + host + ":" + port;
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @PostMapping(value = RestUrlPath.GET_SCHEMAS,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetSchemasResponse getSchemas(@RequestBody GetSchemasRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_SCHEMAS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetSchemasRequest.class)
                    .retrieve()
                    .bodyToMono(GetSchemasResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = RestUrlPath.GET_TABLES,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetTablesResponse getTables(@RequestBody GetTablesRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_TABLES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetTablesRequest.class)
                    .retrieve()
                    .bodyToMono(GetTablesResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = RestUrlPath.GET_COLUMNS,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetColumnsResponse getColumns(@RequestBody GetColumnsRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_COLUMNS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetColumnsRequest.class)
                    .retrieve()
                    .bodyToMono(GetColumnsResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping(value = RestUrlPath.GET_VIEWS,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetViewsResponse getViews(@RequestBody GetViewsRequest request)
    {
        try
        {
            // Use WebClient to call the other REST API
            return webClient.post()
                    .uri(RestUrlPath.GET_VIEWS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), GetViewsRequest.class)
                    .retrieve()
                    .bodyToMono(GetViewsResponse.class)
                    .block(); // block to wait for the response
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
