// 定义 report modal 中关闭图标的事件处理函数
function handleReportModalCloseClick() {
    // 显示 main-area
    var mainArea = document.getElementById('main-area');
    mainArea.style.display = "block";

    // 隐藏 report-area
    document.getElementById('report-area').style.display = "none";

    // 删除所有的 svg
    document.querySelectorAll('#report-area svg').forEach(function(svgElement) {
        svgElement.remove();
    });
}

showReport = function showReportModal()
{
    // 显示 report-area
    var reportArea = document.getElementById('report-area');
    if (reportArea.style.display === 'block') {
        // 如果report-area已经显示，则直接返回，不执行后续操作
        return;
    }
    reportArea.style.display = "block";

    // 隐藏 main-area
    var mainArea = document.getElementById('main-area');
    mainArea.style.display = "none";

    // 为关闭按钮添加点击事件监听器
    var closeButton = document.getElementsByClassName('close')[1];
    closeButton.removeEventListener('click', handleReportModalCloseClick);
    closeButton.addEventListener('click', handleReportModalCloseClick);

    // 获取查询数据并绘制图表
    $.ajax({
        type: 'GET',
        url: '/api/chat/get-query-results',
        success: function (data) {
            // 解析数据
            data.forEach(function (d) {
                d.result = JSON.parse(d.result);
                d.createTime = new Date(d.createTime);
            });

            // 创建 svg
            var overallChartSvg = d3.select('#overall-chart').append("svg");
            var timeChartSvg = d3.select('#time-chart').append("svg");
            var costChartSvg = d3.select('#cost-chart').append("svg");

            // 绘制图表
            drawOverallChart(overallChartSvg, data);
            drawTimeChart(timeChartSvg, data);
            drawCostChart(costChartSvg, data);

            // 添刷刷选功能
            addBrush(overallChartSvg, data, timeChartSvg, costChartSvg);

            // 显示具体 query 信息
            displayQueryInfo(data);
        },
        error: function (error) {
            console.log("Error get query results ", error);
        }
    });
}

function drawOverallChart(svg, data) {
    // 设置 svg 的大小和背景颜色
    svg.attr("viewBox", "0 0 800 200")
        .attr("preserveAspectRatio", "xMidYMid meet")
        .style("background-color", "#F6F8FA");

    // 处理数据，按天分组统计query数量
    var nestedData = d3.group(data, d => d3.timeDay(d.createTime));
    var chartData = Array.from(nestedData, ([key, value]) => ({ date: new Date(key), count: value.length }));

    var margin = { top: 20, right: 30, bottom: 30, left: 40 },
        width = 800 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    var xDomain = chartData.length ? [d3.min(chartData, d => d.date), new Date()] : [new Date(), new Date()];
    var x = d3.scaleTime()
        .domain(xDomain) // 从数据中最早时间到当前时间
        .range([0, width]);

    var yDomain = chartData.length ? [0, d3.max(chartData, d => d.count)] : [0, 1];
    var y = d3.scaleLinear()
        .domain(yDomain)
        .range([height, 0]);

    var timeSpan = (new Date() - d3.min(chartData, d => d.date)) / (1000 * 60 * 60 * 24); // 天数

    var xAxis;
    if (timeSpan <= 7) {
        xAxis = d3.axisBottom(x)
            .ticks(d3.timeDay.every(1))
            .tickFormat(d3.timeFormat("%b %d")); // 天刻度
    } else if (timeSpan <= 30) {
        xAxis = d3.axisBottom(x)
            .ticks(d3.timeDay.every(7))
            .tickFormat(d3.timeFormat("%b %d")); // 每7天刻度
    } else {
        xAxis = d3.axisBottom(x)
            .ticks(d3.timeMonth.every(1))
            .tickFormat(d3.timeFormat("%B")); // 月刻度
    }

    var yAxis = d3.axisLeft(y)
        .ticks(d3.max(chartData, d => d.count)) // 确保显示整数刻度
        .tickSize(-width) // 添加等高线
        .tickPadding(10); // 调整刻度文字与轴线之间的距离

    var area = d3.area()
        .x(d => x(d.date))
        .y0(height)
        .y1(d => y(d.count))
        .curve(d3.curveMonotoneX); // 平滑曲线

    var g = svg.append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    // 添加等高线（放在图层底部）
    g.append("g")
        .attr("class", "grid")
        .call(yAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB"); // 网格线颜色

    // 绘制区域
    g.append("path")
        .datum(chartData)
        .attr("fill", "rgba(139, 192, 156, 0.7)") // 带透明度的颜色
        .attr("d", area);

    var xAxisGroup = g.append("g")
        .attr("transform", `translate(0,${height})`)
        .call(xAxis);

    // 移除默认轴线
    g.selectAll(".domain").remove();

    // 调整纵轴刻度文字与纵轴之间的距离
    g.selectAll(".tick text")
        .attr("dx", "-0.5em");

    // 确保横轴刻度线颜色一致
    xAxisGroup.selectAll(".tick line")
        .attr("stroke", "#CCD4DB"); // 横轴刻度线颜色

    // 确保纵轴刻度线颜色一致
    g.selectAll(".tick line")
        .attr("stroke", "#CCD4DB"); // 纵轴刻度线颜色

    // 移除横轴起点刻度和刻度文字
    xAxisGroup.selectAll(".tick")
        .filter(function (d, i) { return i === 0; })
        .remove();
}

function drawTimeChart(svg, data) {
    svg.attr("viewBox", "0 0 800 200")
        .attr("preserveAspectRatio", "xMidYMid meet")
        .style("background-color", "#F6F8FA");

    var margin = { top: 20, right: 30, bottom: 30, left: 50 },
        width = 800 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    var x = d3.scaleLinear()
        .domain([-0.5, data.length - 0.5])
        .range([0, width]);

    var yDomain = data.length ? [0, d3.max(data, d => d.result.pendingTimeMs + d.result.executionTimeMs)] : [0, 1];
    var y = d3.scaleLinear()
        .domain(yDomain)
        .range([height, 0]);

    var g = svg.append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    var yAxis = d3.axisLeft(y).ticks(10).tickSize(-width).tickFormat(d => `${d}`);
    var yAxisGroup = g.append("g")
        .call(yAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    g.selectAll(".domain").remove();

    yAxisGroup.selectAll(".tick text")
        .attr("x", -10);

    var bars = g.selectAll(".bar")
        .data(data)
        .enter().append("g")
        .attr("class", "bar")
        .attr("transform", (d, i) => `translate(${x(i)}, 0)`);

    bars.append("rect")
        .attr("class", "pending")
        .attr("x", -2)
        .attr("width", 4)
        .attr("y", d => y(d.result.pendingTimeMs))
        .attr("height", d => height - y(d.result.pendingTimeMs))
        .attr("fill", "#98d3c2");

    bars.append("rect")
        .attr("class", "execution")
        .attr("x", -2)
        .attr("width", 4)
        .attr("y", d => y(d.result.pendingTimeMs + d.result.executionTimeMs))
        .attr("height", d => height - y(d.result.executionTimeMs))
        .attr("fill", "#d3a298");

    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    bars.selectAll("rect")
        .on("mouseover", function (event, d) {
            var currentRect = d3.select(this);

            currentRect.attr("fill", "gray");

            tooltip.transition()
                .duration(200)
                .style("opacity", .9);

            var text = `Query: ${d.resultUuid}<br>`;
            text += `Execution Hint: ${d.result.executionHint}<br>`;
            if (currentRect.classed("pending")) {
                text += `Pending Time: ${d.result.pendingTimeMs} ms`;
            } else {
                text += `Execution Time: ${d.result.executionTimeMs} ms`;
            }

            tooltip.html(text)
                .style("left", (event.pageX + 5) + "px")
                .style("top", (event.pageY - 28) + "px");
        })
        .on("mouseout", function (d) {
            var currentRect = d3.select(this);
            if (currentRect.classed("pending")) {
                currentRect.attr("fill", "#98d3c2");
            } else {
                currentRect.attr("fill", "#d3a298");
            }

            tooltip.transition()
                .duration(500)
                .style("opacity", 0);
        });
}

function drawCostChart(svg, data) {
    svg.attr("viewBox", "0 0 800 200")
        .attr("preserveAspectRatio", "xMidYMid meet")
        .style("background-color", "#F6F8FA");

    var margin = { top: 20, right: 30, bottom: 30, left: 50 },
        width = 800 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    var x = d3.scaleLinear()
        .domain([-0.5, data.length - 0.5])
        .range([0, width]);

    var yDomain = data.length ? [0, d3.max(data, d => d.result.billedCents)] : [0, 1];
    var y = d3.scaleLinear()
        .domain(yDomain)
        .range([height, 0]);

    var g = svg.append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    var yAxis = d3.axisLeft(y).ticks(10).tickSize(-width).tickFormat(d => `${d}`);
    var yAxisGroup = g.append("g")
        .call(yAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    g.selectAll(".domain").remove();

    yAxisGroup.selectAll(".tick text")
        .attr("x", -10);

    var points = g.selectAll(".point")
        .data(data)
        .enter().append("circle")
        .attr("class", "point")
        .attr("cx", (d, i) => x(i))
        .attr("cy", d => y(d.result.billedCents))
        .attr("r", 3)
        .attr("fill", "#3182bd");

    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    points.on("mouseover", function (event, d) {
        d3.select(this).attr("r", 5).attr("fill", "gray");

        tooltip.transition()
            .duration(200)
            .style("opacity", .9);

        var text = `Query: ${d.resultUuid}<br>`;
        text += `Billed Cents: ${d.result.billedCents}`;

        tooltip.html(text)
            .style("left", (event.pageX + 5) + "px")
            .style("top", (event.pageY - 28) + "px");
    }).on("mouseout", function (d) {
        d3.select(this).attr("r", 3).attr("fill", "#3182bd");

        tooltip.transition()
            .duration(500)
            .style("opacity", 0);
    });
}

function addBrush(overallChartSvg, data, timeChartSvg, costChartSvg) {
    var margin = { top: 20, right: 30, bottom: 30, left: 40 },
        width = 800 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    var x = d3.scaleTime()
        .domain([d3.min(data, d => d.createTime), new Date()])
        .range([0, width]);

    var brush = d3.brushX()
        .extent([[0, 0], [width, height]])
        .on("brush start", resetCharts)
        .on("brush end", brushed);

    var gBrush = overallChartSvg.append("g")
        .attr("class", "brush")
        .attr("transform", `translate(${margin.left},${margin.top})`)
        .call(brush);

    function resetCharts() {
        // Reset the charts to show all data when brushing starts or ends without selection
        timeChartSvg.selectAll("*").remove();
        drawTimeChart(timeChartSvg, data, null, null);

        costChartSvg.selectAll("*").remove();
        drawCostChart(costChartSvg, data, null, null);
    }

    function brushed(event) {
        var selection = event.selection;
        if (!selection) {
            // If there's no selection, reset the charts to show all data
            resetCharts();
            // Also update query info to reflect all queries
            displayQueryInfo(data);
            return;
        }

        var [x0, x1] = selection.map(x.invert);

        var brushedData = data.filter(d => d.createTime >= x0 && d.createTime <= x1);

        timeChartSvg.selectAll("*").remove();
        drawTimeChart(timeChartSvg, brushedData, x0, x1);

        costChartSvg.selectAll("*").remove();
        drawCostChart(costChartSvg, brushedData, x0, x1);

        // Update query info based on the selected data
        displayQueryInfo(brushedData);
    }
}

function displayQueryInfo(data) {
    var queryInfoDiv = document.getElementById('query-info');
    if (data && data.length != 0) {
        queryInfoDiv.innerHTML = ''; // 清空旧内容
        data.forEach(query => {
            var resultMessage = document.createElement('div');
            resultMessage.className = 'result-message';
            switch (query.result.executionHint.toLowerCase())
            {
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
                    resultMessage.style.backgroundColor= '#e6f7ff'; // 默认颜色
            }
            //  创建状态显示区域
            var statusDisplay = document.createElement('div');
            statusDisplay.className = 'bold-font no-select';
            statusDisplay.innerHTML = `Query Uuid: <span class='result-uuid'>${query.resultUuid}</span>`;
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

            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: '/api/chat/get-sql',
                data: JSON.stringify({"uuid": query.sqlStatementsUuid}),
                success: function (querySQL) {
                    var hightlightedSQL = hljs.highlight(querySQL, {language: "sql", ignoreIllegals: true}).value;
                    var resultDisplayContent = document.createElement('div');
                    var queryDisplay = document.createElement('div');
                    queryDisplay.className = 'query-display';
                    queryDisplay.innerHTML = 'Query: ' + hightlightedSQL;
                    resultDisplayContent.appendChild(queryDisplay);

                    // 添加 executionHint 信息
                    var executionHintDisplay = document.createElement('div');
                    executionHintDisplay.className = 'execution-hint-display';
                    switch (query.result.executionHint.toLowerCase())
                    {
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
                    limitRowsDisplay.textContent = 'LimitRows: ' + query.resultLimit;
                    resultDisplayContent.appendChild(limitRowsDisplay);

                    var columnNames = query.result.columnNames;
                    var rows  = query.result.rows;
                    var columnPrintSizes = query.result.columnPrintSizes;

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
                        <span class="pending-ms">pending: ${query.result.pendingTimeMs} ms</span>
                        <span class="execution-ms">execution: ${query.result.executionTimeMs} ms</span>
                        <span class="cost-cents">cost: ${query.result.billedCents} cents</span>
                    `;
                    resultDisplayContent.appendChild(costDisplay);

                    resultDisplay.appendChild(resultDisplayContent);

                    resultMessage.appendChild(resultDisplay);

                    queryInfoDiv.appendChild(resultMessage);
                },
                error: function (error) {
                    console.log("Error get sql statement ", error);
                }
            });
        });
    }
}
