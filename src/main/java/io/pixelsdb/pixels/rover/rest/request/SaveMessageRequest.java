package io.pixelsdb.pixels.rover.rest.request;

public class SaveMessageRequest
{
    private String uuid;
    private String sqlText;
    private String userMessage;
    private String userMessageUuid;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSqlText() { return sqlText; }

    public void setSqlText(String sqlText) { this.sqlText = sqlText; }

    public String getUserMessage() { return userMessage; }

    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getUserMessageUuid() { return userMessageUuid; }

    public void setUserMessageUuid(String userMessageUuid) { this.userMessageUuid = userMessageUuid; }
}
