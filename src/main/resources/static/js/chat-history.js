function saveSQLStatement(uuid, sqlText)
{
    $.ajax({
        type: 'POST',
        url: '/api/chat/save-sql',
        contentType: 'application/json',
        data: JSON.stringify({"uuid": uuid, "sqlText": sqlText}),
        success: function (response) {
            console.log("SQL statement saved successfully.");
        },
        error: function (error) {
            console.error("Error saving SQL statement: ", error);
        }
    });
}

function updateSQLStatement(uuid, newSQL)
{
    $.ajax({
        type: 'POST',
        url: '/api/chat/update-sql',
        contentType: 'application/json',
        data: JSON.stringify({"uuid": uuid, "newSQL": newSQL}),
        success: function (response) {
            console.log("SQL statement updated successfully.")
        },
        error: function (error) {
            console.log("Error updating SQL statement: ", error);
        }
    });
}

function saveMessage(uuid, sqlText, userMessage, userMessageUuid)
{
    $.ajax({
        type: 'POST',
        url: '/api/chat/save-message',
        contentType: 'application/json',
        data: JSON.stringify({"uuid": uuid, "sqlText": sqlText, "userMessage": userMessage, "userMessageUuid": userMessageUuid}),
        success: function (response) {
            console.log("Message saved successfully.");
        },
        error: function (error) {
            console.log("Error saving message: ", error);
        }
    });
}

function saveQueryResult(uuid, result, resultLimit, resultUuid)
{
    $.ajax({
        type: 'POST',
        url: '/api/chat/save-query-result',
        contentType: 'application/json',
        data: JSON.stringify({"uuid": uuid, "result": result, "resultLimit": resultLimit, "resultUuid": resultUuid}),
        success: function (response) {
            console.log("Query result saved successfully.");
        },
        error: function (error) {
            console.log("Error saving query result ", error);
        }
    });
}
