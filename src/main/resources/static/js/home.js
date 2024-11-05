;
jQuery( function() {
    $("body").on('click','[data-stopPropagation]',function (e) {
        e.stopPropagation();
    });
    
    // 侧边栏
    $(document).on('click', '.pixels-aside-toggler', function() {
        $('.pixels-layout-sidebar').toggleClass('pixels-aside-open');
        $("body").toggleClass('pixels-layout-sidebar-close');
        
        if ($('.pixels-mask-modal').length == 0) {
            $('<div class="pixels-mask-modal"></div>').prependTo('body');
        } else {
            $( '.pixels-mask-modal' ).remove();
        }
    });
  
    // 遮罩层
    $(document).on('click', '.pixels-mask-modal', function() {
        $( this ).remove();
    	$('.pixels-layout-sidebar').toggleClass('pixels-aside-open');
        $('body').toggleClass('pixels-layout-sidebar-close');
    });
    
	// 侧边栏导航
    $(document).on('click', '.nav-item-has-subnav > a', function() {
		$subnavToggle = jQuery( this );
		$navHasSubnav = $subnavToggle.parent();
        $topHasSubNav = $subnavToggle.parents('.nav-item-has-subnav').last();
		$subnav       = $navHasSubnav.find('.nav-subnav').first();
        $viSubHeight  = $navHasSubnav.siblings().find('.nav-subnav:visible').outerHeight();
        $scrollBox    = $('.pixels-layout-sidebar-scroll');
		$navHasSubnav.siblings().find('.nav-subnav:visible').slideUp(500).parent().removeClass('open');
		$subnav.slideToggle( 300, function() {
			$navHasSubnav.toggleClass( 'open' );
			
			// 新增滚动条处理
			var scrollHeight  = 0;
			    pervTotal     = $topHasSubNav.prevAll().length,
			    boxHeight     = $scrollBox.outerHeight(),
		        innerHeight   = $('.sidebar-main').outerHeight(),
                thisScroll    = $scrollBox.scrollTop(),
                thisSubHeight = $(this).outerHeight(),
                footHeight    = 121;
			
			if (footHeight + innerHeight - boxHeight >= (pervTotal * 48)) {
			    scrollHeight = pervTotal * 48;
			}
            if ($subnavToggle.parents('.nav-item-has-subnav').length == 1) {
                $scrollBox.animate({scrollTop: scrollHeight}, 300);
            } else {
                // 子菜单操作
                if (typeof($viSubHeight) != 'undefined' && $viSubHeight != null) {
                    scrollHeight = thisScroll + thisSubHeight - $viSubHeight;
                    $scrollBox.animate({scrollTop: scrollHeight}, 300);
                } else {
                    if ((thisScroll + boxHeight - $scrollBox[0].scrollHeight) == 0) {
                        scrollHeight = thisScroll - thisSubHeight;
                        $scrollBox.animate({scrollTop: scrollHeight}, 300);
                    }
                }
            }
		});
	});
});

// 生成 uuid
function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0,
            v = c == 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
}

showSchemas = function getSchemas() {
    $.ajax({
        type: 'POST',
        url: '/api/metadata/get-schemas',
        contentType: 'application/json',
        data: JSON.stringify({}),
        success: function (response) {
            var schemaMenu = $('#schemaMenu');

            // Clear existing items
            schemaMenu.empty();

            if (Array.isArray(response.schemas) && response.schemas.length > 0) {
                response.schemas.forEach(function (schema) {
                    var listItem = $(`
                        <li class="nav-item nav-item-has-subnav">
                            <a href="#" onclick="showTables('${schema.name}')">${schema.name}</a>
                            <ul class="nav nav-subnav" id="${schema.name}"></ul>
                        </li>
                    `);
                    schemaMenu.append(listItem);
                });
            } else {
                // If no schemas, show an "empty" first-level menu
                var emptySchemaItem = $('<li><a href="#">empty</a></li>');
                schemaMenu.append(emptySchemaItem);
            }
        },
        error: function (error) {
            console.error('Error fetching schemas:', error);
        }
    });
}

// Define getTables function
showTables = function getTables(schema) {
    $.ajax({
        type: 'POST',
        url: '/api/metadata/get-tables',
        contentType: 'application/json',
        data: JSON.stringify({ "schemaName": schema }),
        success: function(response) {
            var tableMenu = $('#' + schema); // Use a unique ID for each schema's table menu

            // Clear existing items
            tableMenu.empty();
            if (Array.isArray(response.tables) && response.tables.length > 0) {
                response.tables.forEach(function(table) {
                    var listItem = $(`
                        <li class="nav-item nav-item-has-subnav">
                            <a href="javascript:void(0)" onclick="showColumns('${schema}', '${table.name}')">${table.name}</a>
                            <ul class="nav nav-subnav" id="${schema}_${table.name}"></ul>
                        </li>
                    `);
                    tableMenu.append(listItem);
                });
            } else {
                // If no tables, show an "empty" second-level menu
                var emptyTableItem = $('<li><a href="#">empty</a></li>');
                tableMenu.append(emptyTableItem);
            }
        },
        error: function(error) {
            console.error('Error fetching tables:', error);
        }
    });
}

// Define showColumns function
showColumns = function getColumns(schema, table) {
    $.ajax({
        type: 'POST',
        url: '/api/metadata/get-columns',
        contentType: 'application/json',
        data: JSON.stringify({ "schemaName": schema, "tableName": table }),
        success: function(response) {
            var columnMenu = $('#' + schema + '_' + table); // Use a unique ID for each table's column menu

            // Clear existing items
            columnMenu.empty();

            if (Array.isArray(response.columns) && response.columns.length > 0) {
                response.columns.forEach(function(column) {
                    var listItem = $(`<li><a href="#" title="${column.type}">${column.name}</a></li>`);
                    columnMenu.append(listItem);
                });
            } else {
                // If no columns, show an "empty" third-level menu
                var emptyColumnItem = $('<li><a href="#">empty</a></li>');
                columnMenu.append(emptyColumnItem);
            }
        },
        error: function(error) {
            console.error('Error fetching columns:', error);
        }
    });
}

$(document).ready(function() {
    $.ajax({
        type: 'POST',
        url: '/api/metadata/get-schemas',
        contentType: 'application/json',
        data: JSON.stringify({}),
        success: function (response) {
            const schemas = response.schemas || [];
            if (schemas.length === 0) {
                $('#schema-select').append('<option value="">Null</option>');
            } else {
                schemas.forEach((schema) => {
                    $('#schema-select').append(`<option value="${schema.name}">${schema.name}</option>`);
                });
            }
        },
        error: function (error) {
            console.error('Error fetching schemas:', error);
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    // Get the input field
    var chatInput = document.getElementById('chat-input');

    // Check if the element exists before adding the event listener
    if (chatInput) {
        // Add an event listener for the "keydown" event
        chatInput.addEventListener('keydown', function (event) {
            // Check if the pressed key is Enter (key code 13)
            if (event.keyCode === 13) {
                // Prevent the default form submission behavior
                event.preventDefault();

                // Call the function to send the message
                sendMessage();
            }
        });
    }
});

// chat-area 自动滚动到底部
function chatAreaScrollToBottom() {
    var chatArea = document.getElementById('chat-area');
    chatArea.scrollTop = chatArea.scrollHeight;
}

// query-status 自动滚到底部
function queryStatusScrollToBottom() {
    var statusArea = document.getElementById('status-area');
    statusArea.scrollTop = statusArea.scrollHeight;
}

let modalMessageID;
let codeMirror;

// 定义modal确认图标的事件处理函数
function handleConfirmClick() {
    var querySQL = document.getElementById('modal-query-sql').innerText;
    var executionHint = document.getElementById('modal-execution-hint-select').value;
    var limit = document.getElementById('modal-output-rows-input').value || 0;
    var resultID = executeQuery(modalMessageID, querySQL, executionHint, limit);

    // 实现message的click处理，高亮对应resultMessage
    var resultMessage = document.getElementById(resultID);
    var systemMessage = document.getElementById(modalMessageID);
    var message = systemMessage.querySelector('.message');
    message.addEventListener('dblclick', function() {
        // 移除.highlight类，然后再添加回来，以重新触发动画
        resultMessage.classList.remove('highlighted');
        setTimeout(function() {
            resultMessage.classList.add('highlighted');
        }, 0); // 使用setTimeout确保在下一个事件循环中添加类
    });

    // 实现result的dblclcik处理，高亮对应systemMessage.message
    resultMessage.addEventListener('dblclick', function (event) {
        // 移除.highlight类，然后再添加回来，以重新触发动画
        message.classList.remove('highlighted');
        setTimeout(function() {
            message.classList.add('highlighted');
        }, 0); // 使用setTimeout确保在下一个事件循环中添加类
    });

    // 执行查询后就隐藏所有icon按钮
    var iconContainer = systemMessage.querySelector('.icon-container');
    iconContainer.style.display = 'none';

    document.getElementById('query-modal').style.display = "none";
}

// 定义 query modal 中关闭图标的事件处理函数
function handleQueryModalCloseClick() {
    document.getElementById('query-modal').style.display = "none";
}

function sendQuery(messageID) {
    var systemMessage = document.getElementById(messageID);
    var messageDiv = systemMessage.querySelector('.message');
    var queryInput = messageDiv.textContent;

    // 如果为空白串直接返回
    if(queryInput.trim() === '') {
        return;
    }

    // 明确打开modal的message
    modalMessageID = messageID;

    // 显示模态窗口
    document.getElementById('query-modal').style.display = "block";
    // 填充查询SQL
    document.getElementById('modal-query-sql').innerHTML = hljs.highlight(queryInput, {language: "sql", ignoreIllegals: true}).value;

    // 为确认图标添加点击事件监听器
    var confirmIcon = document.getElementById('modal-confirm-icon');
    confirmIcon.removeEventListener('click', handleConfirmClick);
    confirmIcon.addEventListener('click', handleConfirmClick);

    // 为关闭按钮添加点击事件监听器
    var closeButton = document.getElementsByClassName('close')[0];
    closeButton.removeEventListener('click', handleQueryModalCloseClick);
    closeButton.addEventListener('click', handleQueryModalCloseClick);
}

function hideMessage(messageID) {
    var systemMessage = document.getElementById(messageID);
    var messageDiv = systemMessage.querySelector('.message');
    var messageEditDiv = systemMessage.querySelector('.message-textarea');
    var iconContainer = systemMessage.querySelector('.icon-container');
    var editIcon = iconContainer.querySelector('.icon[alt="Edit"]');
    var executeIcon = iconContainer.querySelector('.icon[alt="Execute"]');
    var cancelIcon = iconContainer.querySelector('.icon[alt="Cancel"]');
    var confirmIcon = iconContainer.querySelector('.icon[alt="Confirm"]');

    // 隐藏messageDiv,edit和execute按钮
    messageDiv.style.display = 'none';
    editIcon.style.display = 'none';
    executeIcon.style.display = 'none';
    // 显示messageEditDiv,cancel和confirm按钮
    messageEditDiv.style.display = 'block';
    cancelIcon.style.display = 'block';
    confirmIcon.style.display = 'block';
    // 添加codemirror
    codeMirror = CodeMirror.fromTextArea(messageEditDiv, {
        mode: 'text/x-sql',
        lineNumber: false,
        lineWrapping: true,
    });
}

function showMessage(messageID) {
    var systemMessage = document.getElementById(messageID);
    var messageDiv = systemMessage.querySelector('.message');
    var messageEditDiv = systemMessage.querySelector('.message-textarea');
    var iconContainer = systemMessage.querySelector('.icon-container');
    var editIcon = iconContainer.querySelector('.icon[alt="Edit"]');
    var executeIcon = iconContainer.querySelector('.icon[alt="Execute"]');
    var cancelIcon = iconContainer.querySelector('.icon[alt="Cancel"]');
    var confirmIcon = iconContainer.querySelector('.icon[alt="Confirm"]');

    // 隐藏messageEditDiv,cancel和confirm按钮
    messageEditDiv.style.display = 'none';
    cancelIcon.style.display = 'none';
    confirmIcon.style.display = 'none';
    // 显示messageDiv,edit和execute按钮
    messageDiv.style.display = 'block';
    editIcon.style.display = 'block';
    executeIcon.style.display = 'block';
    // 删除codeMirror
    systemMessage.querySelector('.CodeMirror').remove();
}

function editQuery(messageID) {
    var systemMessage = document.getElementById(messageID);
    var messageDiv = systemMessage.querySelector('.message');
    var messageEditDiv = systemMessage.querySelector('.message-textarea');
    // 将messageEditDiv的内容设置成messageDiv的内容
    messageEditDiv.value = messageDiv.textContent;
    // 将messageEditDiv的长宽设置与messageDiv相同
    messageEditDiv.style.width = messageDiv.offsetWidth + 'px';
    messageEditDiv.style.height = messageDiv.offsetHeight + 'px';

    hideMessage(messageID);
}

function cancelEdit(messageID) {
    showMessage(messageID);
}

function confirmEdit(messageID) {
    var systemMessage = document.getElementById(messageID);
    var messageDiv = systemMessage.querySelector('.message');
    var messageEditDiv = systemMessage.querySelector('.message-textarea');
    // 将messageDiv的内容设置成messageEditDiv的内容
    messageDiv.innerHTML = hljs.highlight(codeMirror.getValue(), {language: "sql", ignoreIllegals: true}).value;
    // update sql
    updateSQLStatement(messageID, codeMirror.getValue());
    showMessage(messageID);
}

function sendMessage() {
    var chatInput = document.getElementById('chat-input').value;

    // 如果为空白串直接返回
    if(chatInput.trim() === '') {
        return;
    }

    var chatArea = document.getElementById('chat-area');

    // 恢复 query 默认值
    document.getElementById('chat-input').value = "";

    // 创建一个新的消息元素，代表用户输入的消息
    var userMessageElement = document.createElement('div');
    userMessageElement.className = 'user-message';
    userMessageElement.id = uuid();

    var avatarImage = document.createElement('img');
    avatarImage.className = 'avatar-image';
    avatarImage.src = 'images/users/avatar-cat.jpg';

    var messageDiv = document.createElement('div');
    messageDiv.className = 'message';
    messageDiv.textContent = chatInput;

    userMessageElement.appendChild(avatarImage);
    userMessageElement.appendChild(messageDiv);

    // 将新的消息元素添加到聊天区域
    chatArea.appendChild(userMessageElement);

    chatAreaScrollToBottom();

    const schema = $('#schema-select').val();
    const schemaItemPromises = [];

    // 获取选中schema下的所有表
    $.ajax({
        type: 'POST',
        url: '/api/metadata/get-tables',
        contentType: 'application/json',
        data: JSON.stringify({ "schemaName": schema }),
        success: function (response) {
            const tables = response.tables || [];

            // 遍历所有表，获取每个表的列信息
            tables.forEach((table) => {
                const schemaItemPromise = new Promise((resolve) => {
                    $.ajax({
                        type: 'POST',
                        url: '/api/metadata/get-columns',
                        contentType: 'application/json',
                        data: JSON.stringify({ "schemaName": schema, "tableName": table.name }),
                        success: function (response) {
                            const columns = response.columns || [];
                            const schemaItem = {
                                "table_name": table.name,
                                "table_comment": table.comment || "",
                                "column_names": [],
                                "column_types": [],
                                "column_comments": [],
                                "column_contents": [],
                                "pk_indicators": []
                            };

                            columns.forEach((column) => {
                                schemaItem.column_names.push(column.name);
                                schemaItem.column_types.push(column.type);
                                schemaItem.column_comments.push(column.comment || "");
                                schemaItem.column_contents.push([]);
                                schemaItem.pk_indicators.push(column.is_pk ? 1 : 0);
                            });

                            resolve(schemaItem);
                        },
                        error: function (error) {
                            console.error('Error fetching columns:', error);
                            resolve(null);
                        }
                    });
                });

                schemaItemPromises.push(schemaItemPromise);
            });

            // 所有表的列信息获取完成后，构造data对象
            Promise.all(schemaItemPromises).then((schemaItems) => {
                const data = {
                    "schema": {
                        "schema_items": schemaItems.filter((item) => item !== null),
                        "foreign_keys": []
                    },
                    "text": chatInput
                }
                // text-to-sql
                $.ajax({
                    type: 'POST',
                    url: '/api/query/text-to-sql',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function(response) {
                        // add schema name
                        let querySQL = response.sql.replace(/(\bFROM\b|\bJOIN\b) (\w+)/g, function(match, p1, p2) {
                            return `${p1} ${schema}.${p2}`;
                        });
                        // highlight
                        var hightlightedSQL = hljs.highlight(querySQL, {language: "sql", ignoreIllegals: true}).value;

                        var systemMessage = document.createElement('div');
                        systemMessage.className = 'system-message';
                        systemMessage.id = uuid();

                        // save sql and message
                        saveMessage(systemMessage.id, querySQL, chatInput, userMessageElement.id);

                        var avatarImage = document.createElement('img');
                        avatarImage.className = 'avatar-image';
                        avatarImage.src = 'images/logo-ico.png';
                        systemMessage.appendChild(avatarImage);

                        var messageDiv = document.createElement('div');
                        messageDiv.className = 'message no-select';
                        messageDiv.innerHTML = hightlightedSQL;
                        systemMessage.appendChild(messageDiv);

                        var messageEditDiv = document.createElement('textarea');
                        messageEditDiv.className = 'message-textarea';
                        messageEditDiv.spellcheck = false;
                        messageEditDiv.style.display = 'none';
                        systemMessage.appendChild(messageEditDiv);

                        var iconContainer = document.createElement('div');
                        iconContainer.className = 'icon-container';

                        var editIcon = document.createElement('img');
                        editIcon.src = 'images/edit.svg';
                        editIcon.alt = 'Edit';
                        editIcon.className = 'icon';
                        editIcon.addEventListener('click', function(event) {
                            editQuery(systemMessage.id);
                        });
                        iconContainer.appendChild(editIcon);

                        var executeIcon = document.createElement('img');
                        executeIcon.src = 'images/execute.svg';
                        executeIcon.alt = 'Execute';
                        executeIcon.className = 'icon';
                        executeIcon.addEventListener('click', function(event) {
                            sendQuery(systemMessage.id);
                        });
                        iconContainer.appendChild(executeIcon);

                        var cancelIcon = document.createElement('img');
                        cancelIcon.src = 'images/cancel.svg';
                        cancelIcon.alt = 'Cancel';
                        cancelIcon.className = 'icon';
                        cancelIcon.addEventListener('click', function(event) {
                            cancelEdit(systemMessage.id);
                        });
                        cancelIcon.style.display = 'none';
                        iconContainer.appendChild(cancelIcon);

                        var confirmIcon = document.createElement('img');
                        confirmIcon.src = 'images/confirm.svg';
                        confirmIcon.alt = 'Confirm';
                        confirmIcon.className = 'icon';
                        confirmIcon.addEventListener('click', function(event) {
                           confirmEdit(systemMessage.id);
                        });
                        confirmIcon.style.display = 'none';
                        iconContainer.appendChild(confirmIcon);

                        systemMessage.appendChild(iconContainer);

                        document.getElementById('chat-area').appendChild(systemMessage);

                        chatAreaScrollToBottom();
                    },
                    error: function(error) {
                        console.error("Error: ", error);
                    }
                });
            });

        },
        error: function (error) {
            console.error('Error fetching tables:', error);
        }
    });
}

// 发送后端请求，执行查询
function executeQuery(modalMessageID, query, executionHint, outputRows) {
    // 构建SubmitQueryRequest对象
    var submitQueryRequest = {
        query: query,
        executionHint: executionHint,
        limitRows: outputRows,
    };

    //  创建结果显示区域
    var resultMessage = document.createElement('div');
    resultMessage.className = 'result-message';
    resultMessage.id = uuid();

    // 根据executionHint的不同值设置不同的背景颜色
    switch (Number(executionHint)) {
        case 0: // Best Effort
            resultMessage.style.backgroundColor = '#f3f9e8';
            break;
        case 1: // Relaxed
            resultMessage.style.backgroundColor = '#f9f0e8';
            break;
        case 2: // Immediate
            resultMessage.style.backgroundColor = '#f9e8e8';
            break;
        default:
            resultMessage.style.backgroundColor= '#e6f7ff'; // 默认颜色
    }

    //  创建状态显示区域
    var statusDisplay = document.createElement('div');
    statusDisplay.className = 'query-status no-select';
    statusDisplay.innerHTML = 'Query Status: <span class="pending">PENDING</span>';
    resultMessage.appendChild(statusDisplay);

    //  创建结果显示区域
    var resultDisplay = document.createElement('div');
    resultDisplay.className = 'query-results';
    resultDisplay.style.display = 'none'; //  默认隐藏结果
    resultMessage.appendChild(resultDisplay);

    //  将新的结果显示区域添加到聊天区域
    document.getElementById('status-area').appendChild(resultMessage);

    queryStatusScrollToBottom();

    // 发起submitQuery请求
    fetch('/api/query/submit-query', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(submitQueryRequest),
    })
        .then(response => response.json())
        .then(data => {
            //  如果查询成功，继续处理
            if (data.errorCode === 0) {
                //  显示查询状态
                updateQueryStatusAndResults(modalMessageID, resultMessage.id, outputRows, data.traceToken, submitQueryRequest, statusDisplay, resultDisplay);
            } else {
                //  如果查询失败，显示错误消息
                resultDisplay.textContent = 'Error: ' + data.errorMessage;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 在出错时更新结果显示区域
            resultDisplay.textContent = 'Error occurred during query execution.';
        });
    return resultMessage.id;
}

// 更新查询状态和结果
function updateQueryStatusAndResults(modalMessageID, resultMessageUuid, resultLimit, traceToken, submitQueryRequest, statusDisplay, resultDisplay) {
    // 更新查询状态
    updateQueryStatus(traceToken, function (status) {
        // 更新 status 显示
        var statusSpan = statusDisplay.querySelector('span:first-of-type');
        statusSpan.textContent = status;
        switch (status.toLowerCase()) {
            case 'running':
                statusSpan.classList.remove('pending', 'finished');
                statusSpan.classList.add('running');
                break;
            case 'finished':
                statusSpan.classList.remove('pending', 'running');
                statusSpan.classList.add('finished');
                break;
            default :
                statusSpan.classList.remove('running', 'finished');
                statusSpan.classList.add('pending');
                break;
        }

        // 如果查询状态为 "finished"，获取结果并更新结果显示
        if (status.toLowerCase() === 'finished') {
            //   添加折叠/展开按钮
            var toggleResults = document.createElement('span');
            toggleResults.className = 'toggle-results';
            toggleResults.addEventListener('click', function () {
                if (resultDisplay.style.display === 'none') {
                    resultDisplay.style.display = 'block';
                    toggleResults.classList.add('expanded');
                } else {
                    resultDisplay.style.display = 'none';
                    toggleResults.classList.remove('expanded');
                }
            });

            //  将折叠按钮添加到状态显示区域
            statusDisplay.appendChild(toggleResults);

            getQueryResult(traceToken, function (result) {
                saveQueryResult(modalMessageID, JSON.stringify(result), resultLimit, resultMessageUuid);
                // 显示查询结果
                displayQueryResult(result, submitQueryRequest, statusDisplay, resultDisplay);
            });
        }
    });
}

// 更新查询状态
function updateQueryStatus(traceToken, callback) {
    // 定义标志，判断是否继续刷新状态
    var shouldRefresh = true;

    // 定时刷新查询状态
    var refreshInterval = setInterval(function () {
        if (shouldRefresh) {
            // 构造GetQueryStatusRequest
            var getRequest = {
                traceTokens: [traceToken]
            };

            // 发送请求到后端
            fetch('/api/query/get-query-status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(getRequest),
            })
                .then(response => response.json())
                .then(data => {
                    // 处理后端返回的查询状态
                    if (data.errorCode === 0) {
                        var status = data.queryStatuses[traceToken];

                        // 执行回调函数，传递查询状态
                        if (typeof callback === 'function') {
                            callback(status);
                        }

                        // 如果查询状态为 "finished"，停止刷新
                        if (status.toLowerCase() === 'finished') {
                            shouldRefresh = false;
                            clearInterval(refreshInterval);
                        }
                    } else {
                        console.error('Error getting query status:', data.errorMessage);
                        shouldRefresh = false;
                        clearInterval(refreshInterval);
                    }
                })
                .catch(error => {
                    console.error('Error updating query status:', error);
                    shouldRefresh = false;
                    clearInterval(refreshInterval);
                });
        }
    }, 500); // 每0.5秒刷新一次，根据需求调整
}

// 获取查询结果
function getQueryResult(traceToken, callback) {
    // 发起getQueryResult请求
    fetch('/api/query/get-query-result', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ traceToken: traceToken }),
    })
        .then(response => response.json())
        .then(result => {
            // 执行回调函数，传递查询结果
            if (typeof callback === 'function') {
                callback(result);
            }
        })
        .catch(error => {
            console.error('Error getting query result:', error);
        });
}

// 修改显示查询结果的函数，保留第一行的状态信息
function displayQueryResult(result, submitQueryRequest, statusDisplay, resultDisplay) {
    // 获取显示结果的DOM元素
    var resultDisplayContent = document.createElement('div');

    // 添加 query 信息
    var queryDisplay = document.createElement('div');
    queryDisplay.className = 'query-display';
    queryDisplay.innerHTML = 'Query: ' + hljs.highlight(submitQueryRequest.query, {language: "sql", ignoreIllegals: true}).value;
    resultDisplayContent.appendChild(queryDisplay);

    // 添加 executionHint 信息
    var executionHintDisplay = document.createElement('div');
    executionHintDisplay.className = 'execution-hint-display';
    // 使用switch语句根据executionHint的值选择相应的中文文字
    switch (submitQueryRequest.executionHint) {
        case '0':
            executionHintDisplay.textContent = 'ExecutionHint: Best-of-effort';
            break;
        case '1':
            executionHintDisplay.textContent = 'ExecutionHint: Relaxed';
            break;
        case '2':
            executionHintDisplay.textContent = 'ExecutionHint: Immediate';
            break;
        default:
            executionHintDisplay.textContent = 'ExecutionHint: Unknown';
    }
    resultDisplayContent.appendChild(executionHintDisplay);

    // 添加 limitRow 信息
    var limitRowsDisplay = document.createElement('div')
    limitRowsDisplay.className = 'limit-rows-display';
    limitRowsDisplay.textContent = 'LimitRows: ' + submitQueryRequest.limitRows;
    resultDisplayContent.appendChild(limitRowsDisplay);

    if(result.errorCode !== 0) {
        // 更新 status 显示
        var statusSpan = statusDisplay.querySelector('span:first-of-type');
        statusSpan.textContent = 'FAILED';
        statusSpan.classList.remove('finished');
        statusSpan.classList.add('failed');
        resultDisplayContent.textContent = result.errorMessage;
        resultDisplay.appendChild(resultDisplayContent);
        return;
    }

    // 处理成功的情况
    var columnNames = result.columnNames;
    var rows = result.rows;
    var columnPrintSizes = result.columnPrintSizes;

    // 创建表格元素
    var table = document.createElement('table');
    table.className = 'result-table result-table-bordered';

    // 创建表头
    var thead = document.createElement('thead');
    var headerRow = document.createElement('tr');

    columnNames.forEach(function (columnName, index) {
        var columnPrintSize = Math.max(columnPrintSizes[index], columnName.length);
        var th = document.createElement('th');
        th.textContent = columnName;
        th.style.width = columnPrintSize + 3 + 'ch'; // 增加固定长度
        headerRow.appendChild(th);
    });

    thead.appendChild(headerRow);
    table.appendChild(thead);

    // 创建表体
    var tbody = document.createElement('tbody');

    rows.forEach(function (row) {
        if(row === undefined || row === null) {
            //throw new Error("null row");
            return;
        }
        var tr = document.createElement('tr');

        columnNames.forEach(function (_, index) {
            var td = document.createElement('td');
            var value = row[index];
            if (value === undefined || value === null) {
                value = 'null';
            }
            td.textContent = value;
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    });

    table.appendChild(tbody);
    resultDisplayContent.appendChild(table);

    // 添加costCents信息
    var costDisplay = document.createElement('div');
    costDisplay.className = 'cost-display';
    costDisplay.innerHTML = `
        <span class="pending-ms">pending: ${result.pendingTimeMs} ms</span>
        <span class="execution-ms">execution: ${result.executionTimeMs} ms</span>
        <span class="cost-cents">cost: ${result.billedCents} cents</span>
    `;
    resultDisplayContent.appendChild(costDisplay);

    // 将新的结果显示区域添加到已有的结果显示区域
    resultDisplay.appendChild(resultDisplayContent);
}

document.addEventListener('DOMContentLoaded', function() {
    // 封装拖拽逻辑
    function setupDraggable(rowSelector) {
        const dragbar = document.querySelector(`${rowSelector} .resize`);
        const row = dragbar.parentElement;
        const leftContent = document.querySelector(`${rowSelector} .left-content`);
        const rightContent = document.querySelector(`${rowSelector} .right-content`);

        // 监听mousedown事件，当鼠标按下时开始拖动
        dragbar.addEventListener('mousedown', (e) => {
            e.preventDefault();

            // 获取左右区域的固定偏移值
            const offset = 25; // 假设左右区域的固定偏移值为40px

            // 监听mousemove事件，当鼠标移动时进行拖动
            const onMouseMove = (e) => {
                let deltaX = e.clientX - row.getBoundingClientRect().left - row.offsetWidth / 2;
                if(e.clientX - row.getBoundingClientRect().left <= 60)
                    deltaX = 60 - row.offsetWidth / 2;
                else if(row.getBoundingClientRect().right - e.clientX <= 60)
                    deltaX = row.offsetWidth / 2 - 60;
                const deltaPercentage = deltaX / row.offsetWidth;

                // 设置左右区域的宽度
                leftContent.style.width = `calc(${50 + deltaPercentage * 100}% - ${offset}px)`;
                rightContent.style.width = `calc(${50 - deltaPercentage * 100}% - ${offset}px)`;
            };

            // 监听mouseup事件，当鼠标松开时结束拖动
            const onMouseUp = () => {
                // 移除mousemove和mouseup事件监听器
                document.removeEventListener('mousemove', onMouseMove);
                document.removeEventListener('mouseup', onMouseUp);
            };

            // 添加mousemove和mouseup事件监听器
            document.addEventListener('mousemove', onMouseMove);
            document.addEventListener('mouseup', onMouseUp);
        });
    }
    setupDraggable('#main-area');
    setupDraggable('#report-area');
});

function toggleFullscreen(side) {
    const leftContent = document.querySelector('#main-area .left-content');
    const rightContent = document.querySelector('#main-area .right-content');
    const dragbar = document.querySelector('#main-area .resize');

    if (side === 'left') {
        const fullscreenBtn = document.querySelector('.left-content .fullscreen-btn');
        if (leftContent.classList.contains('fullscreen')) {
            // 如果左边区域已经全屏，则恢复原状
            leftContent.classList.remove('fullscreen');
            leftContent.style.width = 'calc(50% - 25px)';
            rightContent.style.width = 'calc(50% - 25px)';
            leftContent.style.margin = '20px 0 20px 20px';
            rightContent.style.display = 'block';
            dragbar.style.display = 'block';
            fullscreenBtn.src = 'images/fullscreen.svg'; // 切换为全屏图标
        } else {
            // 否则将左边区域设为全屏
            leftContent.classList.add('fullscreen');
            leftContent.style.width = 'calc(100% - 40px)';
            leftContent.style.margin = '20px';
            rightContent.style.display = 'none';
            dragbar.style.display = 'none';
            fullscreenBtn.src = 'images/exitfullscreen.svg'; // 切换为退出全屏图标
        }
    } else if (side === 'right') {
        const fullscreenBtn = document.querySelector('.right-content .fullscreen-btn');
        if (rightContent.classList.contains('fullscreen')) {
            // 如果右边区域已经全屏，则恢复原状
            rightContent.classList.remove('fullscreen');
            rightContent.style.width = 'calc(50% - 25px)';
            leftContent.style.width = 'calc(50% - 25px)';
            rightContent.style.margin = '20px 20px 20px 0';
            leftContent.style.display = 'block';
            dragbar.style.display = 'block';
            fullscreenBtn.src = 'images/fullscreen.svg'; // 切换为全屏图标
        } else {
            // 否则将右边区域设为全屏
            rightContent.classList.add('fullscreen');
            rightContent.style.width = 'calc(100% - 40px)';
            rightContent.style.margin = '20px';
            leftContent.style.display = 'none';
            dragbar.style.display = 'none';
            fullscreenBtn.src = 'images/exitfullscreen.svg'; // 切换为退出全屏图标
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    $.ajax({
        type: 'GET',
        url: '/api/chat/get-chat-history',
        success: function (response) {
            // show chat history messages and query results
            response.forEach(message => {
                (function (message) {
                    var chatArea = document.getElementById('chat-area');

                    // show user's message
                    var userMessageElement = document.createElement('div');
                    userMessageElement.className = 'user-message';
                    userMessageElement.id = message.userMessageUuid;

                    var userAvatarImage = document.createElement('img');
                    userAvatarImage.className = 'avatar-image';
                    userAvatarImage.src = 'images/users/avatar-cat.jpg';

                    var userMessageDiv = document.createElement('div');
                    userMessageDiv.className = 'message';
                    userMessageDiv.textContent = message.userMessage;

                    userMessageElement.appendChild(userAvatarImage);
                    userMessageElement.appendChild(userMessageDiv);

                    chatArea.appendChild(userMessageElement);

                    // show system's message
                    let querySQL = message.sqlStatements;
                    var hightlightedSQL = hljs.highlight(querySQL, { language: "sql", ignoreIllegals: true }).value;

                    var systemMessage = document.createElement("div");
                    systemMessage.className = 'system-message';
                    systemMessage.id = message.sqlStatementsUuid;

                    var sysAvatarImage = document.createElement('img');
                    sysAvatarImage.className = 'avatar-image';
                    sysAvatarImage.src = 'images/logo-ico.png';
                    systemMessage.appendChild(sysAvatarImage);

                    var sysMessageDiv = document.createElement('div');
                    sysMessageDiv.className = 'message no-select';
                    sysMessageDiv.innerHTML = hightlightedSQL;
                    systemMessage.appendChild(sysMessageDiv);

                    if (message.isExecuted == false) // show edit, execute, cancel and confirm button
                    {
                        var sysMessageEditDiv = document.createElement('textarea');
                        sysMessageEditDiv.className = 'message-textarea';
                        sysMessageEditDiv.spellcheck = false;
                        sysMessageEditDiv.style.display = 'none';
                        systemMessage.appendChild(sysMessageEditDiv);

                        var sysIconContainer = document.createElement('div');
                        sysIconContainer.className = 'icon-container';

                        var sysEditIcon = document.createElement('img');
                        sysEditIcon.src = 'images/edit.svg';
                        sysEditIcon.alt = 'Edit';
                        sysEditIcon.className = 'icon';
                        sysEditIcon.addEventListener('click', function (event) {
                            editQuery(message.sqlStatementsUuid);
                        });
                        sysIconContainer.appendChild(sysEditIcon);

                        var sysExecuteIcon = document.createElement('img');
                        sysExecuteIcon.src = 'images/execute.svg';
                        sysExecuteIcon.alt = 'Execute';
                        sysExecuteIcon.className = 'icon';
                        sysExecuteIcon.addEventListener('click', function (event) {
                            sendQuery(message.sqlStatementsUuid);
                        });
                        sysIconContainer.appendChild(sysExecuteIcon);

                        var sysCancelIcon = document.createElement('img');
                        sysCancelIcon.src = 'images/cancel.svg';
                        sysCancelIcon.alt = 'Cancel';
                        sysCancelIcon.className = 'icon';
                        sysCancelIcon.addEventListener('click', function (event) {
                            cancelEdit(message.sqlStatementsUuid);
                        });
                        sysCancelIcon.style.display = 'none';
                        sysIconContainer.appendChild(sysCancelIcon);

                        var sysConfirmIcon = document.createElement('img');
                        sysConfirmIcon.src = 'images/confirm.svg';
                        sysConfirmIcon.alt = 'Confirm';
                        sysConfirmIcon.className = 'icon';
                        sysConfirmIcon.addEventListener('click', function (event) {
                            confirmEdit(message.sqlStatementsUuid);
                        })
                        sysConfirmIcon.style.display = 'none';
                        sysIconContainer.appendChild(sysConfirmIcon);
                        systemMessage.appendChild(sysIconContainer);
                    }
                    else // show query results
                    {
                        var resultMessage = document.createElement('div');
                        resultMessage.className = 'result-message';
                        resultMessage.id = message.resultsUuid;

                        const results = JSON.parse(message.results);

                        switch (results.executionHint.toLowerCase()) {
                            case 'best_of_effort':
                                resultMessage.style.backgroundColor = '#f3f9e8';
                                break;
                            case 'relaxed':
                                resultMessage.style.backgroundColor = '#f9f0e8';
                                break;
                            case 'immediate':
                                resultMessage.style.backgroundColor = '#f9e8e8';
                                break;
                            default:
                                resultMessage.style.backgroundColor = '#e6f7ff'; // 默认颜色
                        }

                        //  创建状态显示区域
                        var statusDisplay = document.createElement('div');
                        statusDisplay.className = 'query-status no-select';
                        statusDisplay.innerHTML = 'Query Status: <span class="finished">FINISHED</span>';
                        resultMessage.appendChild(statusDisplay);

                        // 添加折叠按钮
                        var toggleResults = document.createElement('span');
                        toggleResults.className = 'toggle-results';
                        toggleResults.addEventListener('click', function () {
                            if (resultDisplay.style.display === 'none') {
                                resultDisplay.style.display = 'block';
                                toggleResults.classList.add('expanded');
                            } else {
                                resultDisplay.style.display = 'none';
                                toggleResults.classList.remove('expanded');
                            }
                        });
                        statusDisplay.appendChild(toggleResults);

                        //  创建结果显示区域
                        var resultDisplay = document.createElement('div');
                        resultDisplay.className = 'query-results';
                        resultDisplay.style.display = 'none'; //  默认隐藏结果

                        // 添加 query 信息
                        var resultDisplayContent = document.createElement('div');
                        var queryDisplay = document.createElement('div');
                        queryDisplay.className = 'query-display';
                        queryDisplay.innerHTML = 'Query: ' + hightlightedSQL;
                        resultDisplayContent.appendChild(queryDisplay);

                        // 添加 executionHint 信息
                        var executionHintDisplay = document.createElement('div');
                        executionHintDisplay.className = 'execution-hint-display';
                        switch (results.executionHint.toLowerCase()) {
                            case 'best_of_effort':
                                executionHintDisplay.textContent = 'ExecutionHint: Best-of-effort';
                                break;
                            case 'relaxed':
                                executionHintDisplay.textContent = 'ExecutionHint: Relaxed';
                                break;
                            case 'immediate':
                                executionHintDisplay.textContent = 'ExecutionHint: Immediate';
                                break;
                            default:
                                executionHintDisplay.textContent = 'ExecutionHint: Unknown';
                        }
                        resultDisplayContent.appendChild(executionHintDisplay);

                        // 添加 limitRow 信息
                        var limitRowsDisplay = document.createElement('div')
                        limitRowsDisplay.className = 'limit-rows-display';
                        limitRowsDisplay.textContent = 'LimitRows: ' + message.resultsLimit;
                        resultDisplayContent.appendChild(limitRowsDisplay);

                        if (results.errorCode !== 0) {
                            // 更新 status 显示
                            var statusSpan = statusDisplay.querySelector('span:first-of-type');
                            statusSpan.textContent = 'FAILED';
                            statusSpan.classList.remove('finished');
                            statusSpan.classList.add('failed');
                            resultDisplayContent.textContent = result.errorMessage;
                            resultDisplay.appendChild(resultDisplayContent);
                            return;
                        }

                        var columnNames = results.columnNames;
                        var rows = results.rows;
                        var columnPrintSizes = results.columnPrintSizes;

                        // 创建表格元素
                        var table = document.createElement('table');
                        table.className = 'result-table result-table-bordered';

                        // 创建表头
                        var thead = document.createElement('thead');
                        var headerRow = document.createElement('tr');

                        columnNames.forEach(function (columnName, index) {
                            var columnPrintSize = Math.max(columnPrintSizes[index], columnName.length);
                            var th = document.createElement('th');
                            th.textContent = columnName;
                            th.style.width = columnPrintSize + 3 + 'ch'; // 增加固定长度
                            headerRow.appendChild(th);
                        });

                        thead.appendChild(headerRow);
                        table.appendChild(thead);

                        // 创建表体
                        var tbody = document.createElement('tbody');

                        rows.forEach(function (row) {
                            if (row === undefined || row === null) {
                                //throw new Error("null row");
                                return;
                            }
                            var tr = document.createElement('tr');

                            columnNames.forEach(function (_, index) {
                                var td = document.createElement('td');
                                var value = row[index];
                                if (value === undefined || value === null) {
                                    value = 'null';
                                }
                                td.textContent = value;
                                tr.appendChild(td);
                            });

                            tbody.appendChild(tr);
                        });

                        table.appendChild(tbody);
                        resultDisplayContent.appendChild(table);

                        // 添加costCents信息
                        var costDisplay = document.createElement('div');
                        costDisplay.className = 'cost-display';
                        costDisplay.innerHTML = `
                                    <span class="pending-ms">pending: ${results.pendingTimeMs} ms</span>
                                    <span class="execution-ms">execution: ${results.executionTimeMs} ms</span>
                                    <span class="cost-cents">cost: ${results.billedCents} cents</span>
                                `;
                        resultDisplayContent.appendChild(costDisplay);

                        resultDisplay.appendChild(resultDisplayContent);

                        resultMessage.appendChild(resultDisplay);

                        //  将新的结果显示区域添加到聊天区域
                        document.getElementById('status-area').appendChild(resultMessage);

                        let curSysMessageDiv = sysMessageDiv;
                        let curResultMessage = resultMessage;
                        // 实现message的click处理，高亮对应resultMessage
                        curSysMessageDiv.addEventListener('dblclick', function () {
                            // 移除.highlight类，然后再添加回来，以重新触发动画
                            curResultMessage.classList.remove('highlighted');
                            setTimeout(function () {
                                curResultMessage.classList.add('highlighted');
                            }, 0); // 使用setTimeout确保在下一个事件循环中添加类
                        });

                        // 实现result的dblclcik处理，高亮对应systemMessage.message
                        curResultMessage.addEventListener('dblclick', function (event) {
                            // 移除.highlight类，然后再添加回来，以重新触发动画
                            curSysMessageDiv.classList.remove('highlighted');
                            setTimeout(function () {
                                curSysMessageDiv.classList.add('highlighted');
                            }, 0); // 使用setTimeout确保在下一个事件循环中添加类
                        });

                        queryStatusScrollToBottom();
                    }
                    chatArea.appendChild(systemMessage);
                })(message);
            });
            chatAreaScrollToBottom();
        },
        error: function (error) {
            console.log("Error load history message ", error);
        }
    });
});