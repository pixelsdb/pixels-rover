package io.pixelsdb.pixels.rover.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "query_results")
public class QueryResults
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sql_statements_uuid", nullable = false)
    private String sqlStatementsUuid;

    @Column(name = "result", nullable = false, columnDefinition = "TEXT")
    private String result;

    @Column(name = "result_uuid", nullable = false)
    private String resultUuid;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createTime;

    public QueryResults() { }

    public QueryResults(String sqlStatementsUuid, String result, String resultUuid, Timestamp createTime)
    {
        this.sqlStatementsUuid = sqlStatementsUuid;
        this.result = result;
        this.resultUuid = resultUuid;
        this.createTime = createTime;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getSqlStatementsUuid() { return sqlStatementsUuid; }

    public void setSqlStatementsUuid(String sqlStatementsUuid) { this.sqlStatementsUuid = sqlStatementsUuid; }

    public String getResult() { return result; }

    public void setResult(String result) { this.result = result; }

    public String getResultUuid() { return resultUuid; }

    public void setResultUuid(String resultUuid) { this.resultUuid = resultUuid; }

    public Timestamp getCreateTime() { return createTime; }

    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
}
