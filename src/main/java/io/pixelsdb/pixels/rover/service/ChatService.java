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
package io.pixelsdb.pixels.rover.service;

import io.pixelsdb.pixels.rover.mapper.MessageRepository;
import io.pixelsdb.pixels.rover.mapper.QueryResultsRepository;
import io.pixelsdb.pixels.rover.mapper.SQLStatementsRepository;
import io.pixelsdb.pixels.rover.model.MessageDetail;
import io.pixelsdb.pixels.rover.model.Messages;
import io.pixelsdb.pixels.rover.model.QueryResults;
import io.pixelsdb.pixels.rover.model.SQLStatements;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String getSQLStatement(String uuid)
    {
        SQLStatements sqlStatements = sqlStatementsRepository.findByUuid(uuid);
        if (sqlStatements == null)
        {
            throw new RuntimeException("SQL Statement not found");
        }
        return sqlStatements.getSqlText();
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
    public void saveQueryResult(String uuid, String result, Long resultLimit, String resultUuid)
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
        queryResults.setResultLimit(resultLimit);
        queryResults.setResultUuid(resultUuid);
        queryResults.setCreateTime(new Timestamp(System.currentTimeMillis()));
        queryResultsRepository.save(queryResults);
    }

    @Transactional
    public List<MessageDetail> getAllMessageWithDetails()
    {
        List<Messages> messages = messageRepository.findAll();
        List<MessageDetail> messageDetailList = new ArrayList<>();

        for (Messages message : messages)
        {
            Optional<SQLStatements> sqlStatement = Optional.ofNullable(sqlStatementsRepository.findByUuid(message.getSqlStatementsUuid()));
            Boolean isExecuted = false;
            String results = null;
            String resultsUuid = null;
            Long resultsLimit = null;
            if (sqlStatement.isPresent())
            {
                isExecuted = sqlStatement.get().getIsExecuted();
            }
            if (isExecuted)
            {
                Optional<QueryResults> queryResults = Optional.ofNullable(queryResultsRepository.findBySqlStatementsUuid(message.getSqlStatementsUuid()));
                if (queryResults.isPresent())
                {
                    results = queryResults.get().getResult();
                    resultsLimit = queryResults.get().getResultLimit();
                    resultsUuid = queryResults.get().getResultUuid();
                }
            }
            messageDetailList.add(new MessageDetail(message.getUserMessage(), message.getUserMessageUuid(), sqlStatement.get().getSqlText(),
                    message.getSqlStatementsUuid(), isExecuted, results, resultsLimit, resultsUuid
            ));
        }
        return messageDetailList;
    }

    @Transactional
    public List<QueryResults> getAllQueryResultsOrderByTimestamp()
    {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<QueryResults> queryResults = queryResultsRepository.findAll(sort);
        return queryResults;
    }

    @Transactional
    public List<QueryResults> getQueryResultsBetween(Timestamp start, Timestamp end)
    {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<QueryResults> queryResultsList = queryResultsRepository.findByCreateTimeBetween(start, end, sort);
        return queryResultsList;
    }
}
