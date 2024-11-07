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

import io.pixelsdb.pixels.rover.model.MessageDetail;
import io.pixelsdb.pixels.rover.model.QueryResults;
import io.pixelsdb.pixels.rover.rest.request.*;
import io.pixelsdb.pixels.rover.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController
{
    @Autowired
    private ChatService chatService;

    @PostMapping("/save-sql")
    public void saveSQLStatement(@RequestBody SaveSQLRequest request)
    {
        chatService.saveSQLStatement(request.getUuid(), request.getSqlText());
    }

    @PostMapping("/update-sql")
    public void updateSQLStatement(@RequestBody UpdateSQLRequest request)
    {
        chatService.updateSQLStatement(request.getUuid(), request.getNewSQL());
    }

    @PostMapping("/get-sql")
    public String getSQLStatement(@RequestBody GetSQLRequest request)
    {
        return chatService.getSQLStatement(request.getUuid());
    }

    @PostMapping("/save-message")
    public void saveMessage(@RequestBody SaveMessageRequest request)
    {
        chatService.saveMessage(request.getUuid(), request.getSqlText(), request.getUserMessage(), request.getUserMessageUuid());
    }

    @PostMapping("/save-query-result")
    public void saveQueryResult(@RequestBody SaveQueryResultRequest request)
    {
        chatService.saveQueryResult(request.getUuid(), request.getResult(), request.getResultLimit(), request.getResultUuid());
    }

    @GetMapping("/get-chat-history")
    public ResponseEntity<List<MessageDetail>> getAllMessagesWithDetails()
    {
        List<MessageDetail> detailList = chatService.getAllMessageWithDetails();
        return ResponseEntity.ok(detailList);
    }

    @GetMapping("/get-query-results")
    public ResponseEntity<List<QueryResults>> getAllResultsOrderByTimeStamp()
    {
        List<QueryResults> queryResultsList = chatService.getAllQueryResultsOrderByTimestamp();
        return  ResponseEntity.ok(queryResultsList);
    }

    @PostMapping("/get-query-results-between")
    public ResponseEntity<List<QueryResults>> getResultsBetweenTimeStamp(@RequestBody GetQueryResultsBetweenRequest request)
    {
        List<QueryResults> queryResults = chatService.getQueryResultsBetween(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(queryResults);
    }
}
