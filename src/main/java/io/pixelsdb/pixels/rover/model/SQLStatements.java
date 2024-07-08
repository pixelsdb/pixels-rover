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
