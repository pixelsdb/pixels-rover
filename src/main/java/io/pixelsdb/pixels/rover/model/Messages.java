package io.pixelsdb.pixels.rover.model;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Messages
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_message", nullable = false, columnDefinition = "TEXT")
    private String userMessage;

    @Column(name = "user_message_uuid", nullable = false)
    private String userMessageUuid;

    @Column(name = "sql_statements_uuid", nullable = false)
    private String sqlStatementsUuid;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createTime;

    public Messages() { }

    public Messages(Long id, String userMessage, String userMessageUuid, String sqlStatementsUuid, Timestamp createTime)
    {
        this.userMessage = userMessage;
        this.userMessageUuid = userMessageUuid;
        this.sqlStatementsUuid = sqlStatementsUuid;
        this.createTime = createTime;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUserMessage() { return userMessage; }

    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getUserMessageUuid() { return userMessageUuid; }

    public void setUserMessageUuid(String userMessageUuid) { this.userMessageUuid = userMessageUuid; }

    public String getSqlStatementsUuid() { return sqlStatementsUuid; }

    public void setSqlStatementsUuid(String sqlStatementsUuid) { this.sqlStatementsUuid = sqlStatementsUuid; }

    public Timestamp getCreateTime() { return createTime; }

    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
}
