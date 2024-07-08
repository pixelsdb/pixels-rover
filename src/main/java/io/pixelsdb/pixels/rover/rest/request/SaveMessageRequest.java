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
package io.pixelsdb.pixels.rover.rest.request;

public class SaveMessageRequest
{
    private String uuid;
    private String sqlText;
    private String userMessage;
    private String userMessageUuid;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getSqlText() { return sqlText; }

    public void setSqlText(String sqlText) { this.sqlText = sqlText; }

    public String getUserMessage() { return userMessage; }

    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getUserMessageUuid() { return userMessageUuid; }

    public void setUserMessageUuid(String userMessageUuid) { this.userMessageUuid = userMessageUuid; }
}
