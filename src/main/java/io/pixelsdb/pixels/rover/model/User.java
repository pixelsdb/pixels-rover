/*
 * Copyright 2023 PixelsDB.
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
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created at: 4/3/23
 * Author: hank
 */
@Entity
public class User
{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String email;
    private String affiliation;
    private String password;
    private Timestamp createTime;

    public User(String username, String name, String email,
                String affiliation, String password, Timestamp createTime)
    {
        this.username = username;
        this.name = name;
        this.email = email;
        this.affiliation = affiliation;
        this.password = password;
        this.createTime = createTime;
    }

    public User() { }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.username, this.email);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User user = (User) obj;
        return Objects.equals(this.id, user.id) && Objects.equals(this.name, user.name) &&
                Objects.equals(this.email, user.email);
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }

    public Long getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getAffiliation()
    {
        return affiliation;
    }

    @JSONField(serialize = false)
    public String getPassword()
    {
        return password;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }
}
