This is a Java project that executes a Node.js script to create an SVG file.

We are using Trireme, a "fake Node.js" instance to invoke a Node.js instance from the JVM.


Apache ECharts is a JavaScript library used for visualizing a model as a chart. The Trireme Node instance hosts the chart
as an SVG.


# How to run
1. `npm install --g trireme`
2. `mvn clean compile`
3. Execute the main function inside `App.java`