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

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created at: 4/3/23
 * Author: hank
 */
@Entity
public class LoginUser
{
    @Id
    private Long id;
    private String name;
    private String password;

    public LoginUser(Long id, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public LoginUser()
    {

    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!(obj instanceof LoginUser))
            return false;
        LoginUser loginUser = (LoginUser) obj;
        return Objects.equals(this.id, loginUser.id) && Objects.equals(this.name, loginUser.name);
    }

    @Override
    public String toString()
    {
        return "LoginUser{" + "id=" + this.id + ", name='" + this.name + "'}";
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
