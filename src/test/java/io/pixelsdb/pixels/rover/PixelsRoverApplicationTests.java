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
package io.pixelsdb.pixels.rover;

import io.pixelsdb.pixels.rover.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.pixelsdb.pixels.rover.model.SQLStatements;
import io.pixelsdb.pixels.rover.model.Messages;
import io.pixelsdb.pixels.rover.model.QueryResults;
import io.pixelsdb.pixels.rover.mapper.SQLStatementsRepository;
import io.pixelsdb.pixels.rover.mapper.MessageRepository;
import io.pixelsdb.pixels.rover.mapper.QueryResultsRepository;

@SpringBootTest
class PixelsRoverApplicationTests
{
	@Autowired
	private ChatService chatService;

	@Test
	void SQLStatementsTest()
	{
		String uuid = "xxxxxxxx-xx11-4xxx-yxxx-xxxxxxxxxxxx";
		String sqlText = "select * from nation";
		chatService.saveSQLStatement(uuid, sqlText);
	}

	@Test
	void MessageTest()
	{

	}

	@Test
	void ResultTest()
	{

	}
}
