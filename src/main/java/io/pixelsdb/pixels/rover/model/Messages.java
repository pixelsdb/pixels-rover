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
