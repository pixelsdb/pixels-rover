package io.pixelsdb.pixels.rover.controller;

import io.pixelsdb.pixels.rover.rest.request.SaveMessageRequest;
import io.pixelsdb.pixels.rover.rest.request.SaveQueryResultRequest;
import io.pixelsdb.pixels.rover.rest.request.SaveSQLRequest;
import io.pixelsdb.pixels.rover.rest.request.UpdateSQLRequest;
import io.pixelsdb.pixels.rover.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/save-message")
    public void saveMessage(@RequestBody SaveMessageRequest request)
    {
        chatService.saveMessage(request.getUuid(), request.getSqlText(), request.getUserMessage(), request.getUserMessageUuid());
    }

    @PostMapping("/save-query-result")
    public void saveQueryResult(@RequestBody SaveQueryResultRequest request)
    {
        chatService.saveQueryResult(request.getUuid(), request.getResult(), request.getResultUuid());
    }
}
