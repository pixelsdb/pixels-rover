package io.pixelsdb.pixels.rover.model;

public class MessageDetail
{
    private String userMessage;
    private String userMessageUuid;
    private String sqlStatements;
    private String sqlStatementsUuid;
    private boolean isExecuted;
    private String results;

    public MessageDetail() { }

    public MessageDetail(String userMessage, String userMessageUuid, String sqlStatements, String sqlStatementsUuid, boolean isExecuted, String results)
    {
        this.userMessage = userMessage;
        this.userMessageUuid = userMessageUuid;
        this.sqlStatements = sqlStatements;
        this.sqlStatementsUuid = sqlStatementsUuid;
        this.isExecuted = isExecuted;
        this.results = results;
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

    public void setResults() { this.results = results; }
}
