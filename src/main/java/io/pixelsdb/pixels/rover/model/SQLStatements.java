package io.pixelsdb.pixels.rover.model;

import com.alibaba.fastjson.JSON;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "sql_statements")
public class SQLStatements
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "sql_text", nullable = false, columnDefinition = "TEXT")
    private String sqlText;

    @Column(name = "is_modified", nullable = false)
    private Boolean isModified = false;

    @Column(name = "is_executed", nullable = false)
    private Boolean isExecuted = false;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private  Timestamp createTime;

    public SQLStatements() { }

    public SQLStatements(String uuid, String sqlText, Boolean isModified, Boolean isExecuted, Timestamp createTime)
    {
        this.uuid = uuid;
        this.sqlText = sqlText;
        this.isModified = isModified;
        this.isExecuted = isExecuted;
        this.createTime = createTime;
    }

    @Override
    public  String toString() { return JSON.toJSONString(this); }

    public Long getId() { return id; }

    public void setID(Long id) { this.id = id; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSqlText() { return sqlText; }

    public void setSqlText(String sqlText) { this.sqlText = sqlText; }

    public Boolean getIsModified() { return isModified; }

    public void setIsModified(Boolean isModified) { this.isModified = isModified; }

    public Boolean getIsExecuted() { return isExecuted; }

    public void setIsExecuted(Boolean isExecuted) { this.isExecuted = isExecuted; }

    public Timestamp getCreateTime() { return createTime; }

    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
}
