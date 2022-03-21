This is a Java project that executes a Node.js script to create an SVG file.

We are using Trireme, a "fake Node.js" instance to invoke a Node.js instance from the JVM.

Apache ECharts is a JavaScript library used for visualizing a model as a chart. The Trireme Node instance hosts the chart
as an SVG.

The project is bundled into an executable JAR file using the Apache Maven Assembly Plugin, which allows us to bundle all dependencies at once. Finally, the main class defined in the JAR file is executed using the `java` command.

# How to run
1. `mvn clean compile assembly:single`
2. `java -cp target/trireme-java-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App`