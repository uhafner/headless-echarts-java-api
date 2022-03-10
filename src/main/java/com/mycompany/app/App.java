package com.mycompany.app;
import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Inside the JVM, this project runs a "proof of concept" Hello World console output in Node and a static ECharts project
 */
public class App {
    public static void main( String[] args ) throws NodeException, ExecutionException, InterruptedException {
        System.out.println( "Hello World!" );
        NodeEnvironment env = new NodeEnvironment();

        //NodeScript installScript = env.createScript("entry.js", new File("src/echarts/entry.js"), null);

        NodeScript pocScript = env.createScript("hello.js", new File("src/js/hello.js"), null);
        NodeScript echartsScript = env.createScript("index.js", new File("src/echarts/src/index.js"), null);

        // Wait for the script to complete
        ScriptStatus pocStatus = pocScript.execute().get();
        System.out.println("Launching ECharts on port 8080");
        ScriptStatus echartsStatus = echartsScript.execute().get();

        // Check the exit code
        System.exit(pocStatus.getExitCode());
        System.exit(echartsStatus.getExitCode());
    }
}
