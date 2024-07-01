// 定义 report modal 中关闭图标的事件处理函数
function handleReportModalCloseClick() {
    document.getElementById('report-modal').style.display = "none";

    // 删除所有的 svg
    document.querySelectorAll('#report-modal svg').forEach(function(svgElement) {
        svgElement.remove();
    });
}

showReport = function showReportModal()
{
    var reportModal = document.getElementById('report-modal');

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

            console.log(data);

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
        },
        error: function (error) {
            console.log("Error get query results ", error);
        }
    })

    // 显示模态窗口
    reportModal.style.display = "block";

    // 为关闭按钮添加点击事件监听器
    var closeButton = document.getElementsByClassName('close')[1];
    closeButton.removeEventListener('click', handleReportModalCloseClick);
    closeButton.addEventListener('click', handleReportModalCloseClick);
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

function drawTimeChart(svg, data, x0 = d3.min(data, d => d.createTime), x1 = new Date()) {
    // 设置 svg 的大小和背景颜色
    svg.attr("viewBox", "0 0 400 200")
        .attr("preserveAspectRatio", "xMidYMid meet")
        .style("background-color", "#F6F8FA");

    var margin = { top: 20, right: 30, bottom: 30, left: 50 },
        width = 400 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    // 横轴： createTime
    var x = d3.scaleTime()
        .domain([x0, x1]) // 从数据中最早时间到当前时间
        .range([0, width]);

    // 纵轴：时间长度（ms）
    var yDomain = data.length ? [0, d3.max(data, d => d.result.pendingTimeMs + d.result.executionTimeMs)] : [0, 1];
    var y = d3.scaleLinear()
        .domain(yDomain)
        .range([height, 0]);

    var g = svg.append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    // 绘制横轴
    var xAxis = d3.axisBottom(x).tickFormat(d3.timeFormat("%b %d")).ticks(d3.timeDay.every(1));
    var xAxisGroup = g.append("g")
        .attr("transform", `translate(0,${height})`)
        .call(xAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    // 绘制等高线并标注 ms
    var yAxis = d3.axisLeft(y).ticks(10).tickSize(-width).tickFormat(d => `${d}`);
    var yAxisGroup = g.append("g")
        .call(yAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    g.selectAll(".domain").remove(); // 移除轴线

    // 移动等高线标签位置
    yAxisGroup.selectAll(".tick text")
        .attr("x", -10);

    // // 添加图注
    // g.append("text")
    //     .attr("x", width - 60)
    //     .attr("y", -10)
    //     .attr("text-anchor", "end")
    //     .text("Time (ms)");

    // 绘制甘特图
    var bars = g.selectAll(".bar")
        .data(data)
        .enter().append("g")
        .attr("class", "bar")
        .attr("transform", d => `translate(${x(d.createTime)}, 0)`);

    // 绘制 pendingTime 部分
    bars.append("rect")
        .attr("class", "pending")
        .attr("x", -2) // 给每个条形添加一些宽度
        .attr("width", 4) // 调整条形宽度
        .attr("y", d => y(d.result.pendingTimeMs))
        .attr("height", d => height - y(d.result.pendingTimeMs))
        .attr("fill", "#98d3c2");

    // 绘制 executionTime 部分
    bars.append("rect")
        .attr("class", "execution")
        .attr("x", -2) // 给每个条形添加一些宽度
        .attr("width", 4) // 调整条形宽度
        .attr("y", d => y(d.result.pendingTimeMs + d.result.executionTimeMs))
        .attr("height", d => height - y(d.result.executionTimeMs))
        .attr("fill", "#d3a298");

    // 添加悬停提示
    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    tooltip.style("position", "absolute")
        .style("text-align", "center")
        .style("width", "180px")
        .style("height", "70px")
        .style("padding", "2px")
        .style("font", "12px sans-serif")
        .style("background", "lightsteelblue")
        .style("border", "0px")
        .style("border-radius", "8px")
        .style("pointer-events", "none");

    // 悬停交互
    bars.selectAll("rect")
        .on("mouseover", function (event, d) {
            var currentRect = d3.select(this);

            currentRect.attr("fill", "gray");

            tooltip.transition()
                .duration(200)
                .style("opacity", .9);

            var text = `Query: ${d.sqlStatementsUuid}<br>`;
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

function drawCostChart(svg, data, x0 = d3.min(data, d => d.createTime), x1 = new Date()) {
    // 设置 svg 的大小和背景颜色
    svg.attr("viewBox", "0 0 400 200")
        .attr("preserveAspectRatio", "xMidYMid meet")
        .style("background-color", "#F6F8FA");

    var margin = { top: 20, right: 30, bottom: 30, left: 50 },
        width = 400 - margin.left - margin.right,
        height = 200 - margin.top - margin.bottom;

    // 横轴： createTime
    var x = d3.scaleTime()
        .domain([x0, x1]) // 从数据中最早时间到当前时间
        .range([0, width]);

    // 纵轴： billedCents
    var yDomain = data.length ? [0, d3.max(data, d => d.result.billedCents)] : [0, 1];
    var y = d3.scaleLinear()
        .domain(yDomain)
        .range([height, 0]);

    var g = svg.append("g")
        .attr("transform", `translate(${margin.left},${margin.top})`);

    // 绘制横轴
    var xAxis = d3.axisBottom(x).tickFormat(d3.timeFormat("%b %d")).ticks(d3.timeDay.every(1));
    var xAxisGroup = g.append("g")
        .attr("transform", `translate(0,${height})`)
        .call(xAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    // 绘制等高线并标注
    var yAxis = d3.axisLeft(y).ticks(10).tickSize(-width).tickFormat(d => `${d}`);
    var yAxisGroup = g.append("g")
        .call(yAxis)
        .selectAll(".tick line")
        .attr("stroke", "#CCD4DB");

    g.selectAll(".domain").remove(); // 移除轴线

    // 移动等高线标签位置
    yAxisGroup.selectAll(".tick text")
        .attr("x", -10);

    // // 添加图注
    // g.append("text")
    //     .attr("x", width - 60)
    //     .attr("y", -10)
    //     .attr("text-anchor", "end")
    //     .text("Billed Cents");

    // 绘制散点图
    var points = g.selectAll(".point")
        .data(data)
        .enter().append("circle")
        .attr("class", "point")
        .attr("cx", d => x(d.createTime))
        .attr("cy", d => y(d.result.billedCents))
        .attr("r", 3)
        .attr("fill", "#3182bd");

    // 添加悬停提示
    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);

    tooltip.style("position", "absolute")
        .style("text-align", "center")
        .style("width", "180px")
        .style("height", "70px")
        .style("padding", "2px")
        .style("font", "12px sans-serif")
        .style("background", "lightsteelblue")
        .style("border", "0px")
        .style("border-radius", "8px")
        .style("pointer-events", "none");

    // 悬停交互
    points.on("mouseover", function (event, d) {
        d3.select(this).attr("r", 5).attr("fill", "gray");

        tooltip.transition()
            .duration(200)
            .style("opacity", .9);

        var text = `Query: ${d.sqlStatementsUuid}<br>`;
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
        width = overallChartSvg.attr("width") - margin.left - margin.right,
        height = overallChartSvg.attr("height") - margin.top - margin.bottom;

    var x = d3.scaleTime()
        .domain([d3.min(data, d => d.createTime), new Date()])
        .range([0, width]);

    var brush = d3.brushX()
        .extent([[0, 0], [width, height]])
        .on("brush end", brushed);

    var gBrush = overallChartSvg.append("g")
        .attr("class", "brush")
        .attr("transform", `translate(${margin.left},${margin.top})`)
        .call(brush);

    function brushed(event) {
        var selection = event.selection;
        if (!selection) return;

        var [x0, x1] = selection.map(x.invert);

        var brushedData = data.filter(d => d.createTime >= x0 && d.createTime <= x1);

        timeChartSvg.selectAll("*").remove();
        drawTimeChart(timeChartSvg, brushedData, x0, x1);

        costChartSvg.selectAll("*").remove();
        drawCostChart(costChartSvg, brushedData, x0, x1);
    }
}
