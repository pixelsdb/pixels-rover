package io.pixelsdb.pixels.rover.rest.response;

public class TextToSQLResponse {
    private double latency;
    private String sql;

    public TextToSQLResponse() { }

    public TextToSQLResponse(double latency, String sql) {
        this.latency = latency;
        this.sql = sql;
    }

    public double getLatency() { return latency; }

    public void setLatency(double latency) { this.latency = latency; }

    public String getSql() { return sql; }

    public void setSql(String sql) { this.sql = sql; }
}
