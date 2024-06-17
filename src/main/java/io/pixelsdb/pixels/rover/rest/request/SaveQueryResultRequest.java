package io.pixelsdb.pixels.rover.rest.request;

public class SaveQueryResultRequest
{
    private String uuid;
    private String result;
    private String resultUuid;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }

    public String getResultUuid() { return resultUuid; }

    public void setResultUuid(String resultUuid) { this.resultUuid = resultUuid; }
}
