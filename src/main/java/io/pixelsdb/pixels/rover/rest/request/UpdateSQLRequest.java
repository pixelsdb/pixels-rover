package io.pixelsdb.pixels.rover.rest.request;

public class UpdateSQLRequest
{
    private String uuid;
    private String newSQL;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getNewSQL() { return newSQL; }

    public void setNewSQL(String newSQL) { this.newSQL = newSQL; }
}
