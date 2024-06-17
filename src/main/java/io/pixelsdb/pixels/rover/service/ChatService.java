package io.pixelsdb.pixels.rover.service;

import io.pixelsdb.pixels.rover.mapper.MessageRepository;
import io.pixelsdb.pixels.rover.mapper.QueryResultsRepository;
import io.pixelsdb.pixels.rover.mapper.SQLStatementsRepository;
import io.pixelsdb.pixels.rover.model.Messages;
import io.pixelsdb.pixels.rover.model.QueryResults;
import io.pixelsdb.pixels.rover.model.SQLStatements;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ChatService
{
    @Autowired
    private SQLStatementsRepository sqlStatementsRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private QueryResultsRepository queryResultsRepository;

    @Transactional
    public void saveSQLStatement(String uuid, String sqlText)
    {
        SQLStatements sqlStatements = new SQLStatements();
        sqlStatements.setUuid(uuid);
        sqlStatements.setSqlText(sqlText);
        sqlStatements.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sqlStatementsRepository.save(sqlStatements);
    }

    @Transactional
    public void updateSQLStatement(String uuid, String newSQL)
    {
        SQLStatements sqlStatements = sqlStatementsRepository.findByUuid(uuid);
        if (sqlStatements == null)
        {
            throw new RuntimeException("SQL Statement not found");
        }
        sqlStatements.setSqlText(newSQL);
        sqlStatements.setIsModified(true);
        sqlStatementsRepository.save(sqlStatements);
    }

    @Transactional
    public void saveMessage(String uuid, String sqlText, String userMessage, String userMessageUuid)
    {
        // save sql
        SQLStatements sqlStatements = new SQLStatements();
        sqlStatements.setUuid(uuid);
        sqlStatements.setSqlText(sqlText);
        sqlStatements.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sqlStatementsRepository.save(sqlStatements);

        // save message
        Messages messages = new Messages();
        messages.setUserMessage(userMessage);
        messages.setUserMessageUuid(userMessageUuid);
        messages.setSqlStatementsUuid(uuid);
        messages.setCreateTime(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(messages);
    }

    @Transactional
    public void saveQueryResult(String uuid, String result, String resultUuid)
    {
        // update sql
        SQLStatements sqlStatements = sqlStatementsRepository.findByUuid(uuid);
        if (sqlStatements == null)
        {
            throw new RuntimeException("SQL Statement not found");
        }
        sqlStatements.setIsExecuted(true);
        sqlStatementsRepository.save(sqlStatements);

        // save result
        QueryResults queryResults = new QueryResults();
        queryResults.setSqlStatementsUuid(uuid);
        queryResults.setResult(result);
        queryResults.setResultUuid(resultUuid);
        queryResults.setCreateTime(new Timestamp(System.currentTimeMillis()));
        queryResultsRepository.save(queryResults);
    }
}
