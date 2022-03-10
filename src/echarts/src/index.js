var http = require("http");
var echarts = require("echarts");

function renderChart() {
  const chart = echarts.init(null, null, {
    renderer: "svg",
    ssr: true,
    width: 400,
    height: 300
  });

  chart.setOption({
    xAxis: {
      type: "category",
      data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
    },
    yAxis: {
      type: "value"
    },
    series: [
      {
        data: [120, 200, 150, 80, 70, 110, 130],
        type: "bar"
      }
    ]
  });

  return chart.renderToSVGString();
}

http
  .createServer(function (req, res) {
    res.writeHead(200, {
      "Content-Type": "application/xml"
    });
    res.write(renderChart());
    res.end();
  })
  .listen(8080);