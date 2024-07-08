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
package io.pixelsdb.pixels.rover.model;

public class MessageDetail
{
    private String userMessage;
    private String userMessageUuid;
    private String sqlStatements;
    private String sqlStatementsUuid;
    private boolean isExecuted;
    private String results;
    private Long resultsLimit;
    private String resultsUuid;

    public MessageDetail() { }

    public MessageDetail(String userMessage, String userMessageUuid, String sqlStatements, String sqlStatementsUuid,
                         boolean isExecuted, String results, Long resultsLimit, String resultsUuid)
    {
        this.userMessage = userMessage;
        this.userMessageUuid = userMessageUuid;
        this.sqlStatements = sqlStatements;
        this.sqlStatementsUuid = sqlStatementsUuid;
        this.isExecuted = isExecuted;
        this.results = results;
        this.resultsLimit = resultsLimit;
        this.resultsUuid = resultsUuid;
    }

    public String getUserMessage() { return userMessage; }

    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getUserMessageUuid() { return userMessageUuid; }

    public void setUserMessageUuid(String userMessageUuid) { this.userMessageUuid = userMessageUuid; }

    public String getSqlStatements() { return sqlStatements; }

    public void setSqlStatements(String sqlStatements) { this.sqlStatements = sqlStatements; }

    public String getSqlStatementsUuid() { return sqlStatementsUuid; }

    public void setSqlStatementsUuid(String sqlStatementsUuid) { this.sqlStatementsUuid = sqlStatementsUuid; }

    public Boolean getIsExecuted() { return isExecuted; }

    public void setExecuted(Boolean isExecuted) { this.isExecuted = isExecuted; }

    public String getResults() { return results; }

    public void setResults(String results) { this.results = results; }

    public Long getResultsLimit() { return resultsLimit; }

    public void setResultsLimit(Long resultsLimit) { this.resultsLimit = resultsLimit; }

    public String getResultsUuid() { return resultsUuid; }

    public void setResultsUuid(String resultsUuid) { this.resultsUuid = resultsUuid; }
}
