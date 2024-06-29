package io.pixelsdb.pixels.rover.rest.request;

public class SaveSQLRequest
{
    private String uuid;
    private String sqlText;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSqlText() { return sqlText; }

    public void setSqlText(String sqlText) { this.sqlText = sqlText; }
}
