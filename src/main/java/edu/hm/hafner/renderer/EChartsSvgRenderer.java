package edu.hm.hafner.renderer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.hafner.renderer.util.SvgParser;
import edu.hm.hafner.renderer.util.TriremeResourcesProvider;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Returns an SVG as string, which is rendered using two string parameters specifying the configuration and export
 * options for the desired chart.
 * Example API call: String mySvgChart = echartsSvgRenderer.createSvgString(configurationOptions, exportOptions);
 */
public class EChartsSvgRenderer {

    private static final String ECHARTS_PATH = "/echarts/";

    private static final String JAVASCRIPT_FILENAME = "chartRenderer.js";

    private static final String NODE_MODULES_FOLDER_NAME =  "node_modules";

    private static final String SVG_FILENAME = "echarts-render.svg";

    /**
     * Checks if the JSON parameters are valid.
     * @param configJson Chart configuration JSON parameter
     * @param exportJson Export configuration JSON parameter
     */
    private void validateJsons(String configJson, String exportJson) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode configJsonNode;
        try {
            configJsonNode = mapper.readTree(configJson);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Chart configuration parameter has invalid JSON.");
        }
        JsonNode seriesNode = configJsonNode.get("series");
        if (seriesNode == null) {
            throw new IllegalArgumentException("Series key missing in chart configuration parameter.");
        }
        if (!seriesNode.isArray() || seriesNode.isEmpty()) {
            throw new IllegalArgumentException("Invalid series value in the chart configuration parameter.");
        }

        JsonNode exportJsonNode;
        try {
            exportJsonNode = mapper.readTree(exportJson);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Export configuration parameter has invalid JSON.");
        }
        JsonNode widthNode = exportJsonNode.get("width");
        JsonNode heightNode = exportJsonNode.get("height");
        if (widthNode == null || heightNode == null) {
            throw new IllegalArgumentException("Width and/or height missing in export configuration parameter.");
        }
        if (widthNode.asInt() == 0 || heightNode.asInt() == 0) {
            throw new IllegalArgumentException("Invalid width and/or height values in export configuration parameter.");
        }
    }

    /**
     * Returns an SVG as string by using two options string parameters.
     * @param configOptions SVG configuration option for modelling the SVG such as chart type, data to display, etc.
     * @param exportOptions SVG export options, specifying dimensions, rendererMode, etc.
     * @return SVG as string
     */
    public String createSvgString(String configOptions, String exportOptions) {
        if ((configOptions == null || configOptions.isEmpty()) && (exportOptions == null || exportOptions.isEmpty())) {
            throw new IllegalArgumentException("No parameters were provided");
        }
        if (configOptions == null || configOptions.isEmpty()) {
            throw new IllegalArgumentException("Chart configuration parameter is missing.");
        }
        if (exportOptions == null || exportOptions.isEmpty()) {
            throw new IllegalArgumentException("Export configuration parameter is missing.");
        }
        validateJsons(configOptions, exportOptions);

        String triremeWorkingDirectoryPath;

        try {
            final TriremeResourcesProvider triremeResourcesProvider = new TriremeResourcesProvider();
            final File eChartsFile =
                    triremeResourcesProvider.copyJavaScriptFile(ECHARTS_PATH + JAVASCRIPT_FILENAME);
            if (!eChartsFile.isFile()) {
                throw new FileNotFoundException("Failed to load rendering scripts due to incorrect installation.");
            }
            triremeWorkingDirectoryPath =
                    triremeResourcesProvider.copyNodeModulesFolder(ECHARTS_PATH + NODE_MODULES_FOLDER_NAME);
            if (triremeWorkingDirectoryPath.length() < 1) {
                throw new FileNotFoundException("Failed to load Node.js scripts due to incorrect installation.");
            }

            // Array length set by both options parameters, SVG file name and file path of Node.js resources
            String[] triremeParameters = new String[4];
            triremeParameters[0] = configOptions;
            triremeParameters[1] = exportOptions;
            triremeParameters[2] = SVG_FILENAME;
            triremeParameters[triremeParameters.length - 1] = triremeWorkingDirectoryPath;

            final NodeEnvironment nodeEnv = new NodeEnvironment();
            NodeScript echartsInstance = nodeEnv.createScript(JAVASCRIPT_FILENAME, eChartsFile, triremeParameters);
            if (echartsInstance == null) {
                throw new IllegalStateException("Failed to execute rendering due to system errors.");
            }
            echartsInstance.execute().get();
        } catch (NodeException | IOException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed to execute rendering due to system errors.", e);
        }

        final SvgParser svgParser = new SvgParser();
        String svgAsString = svgParser.parseSvgAsString(triremeWorkingDirectoryPath, SVG_FILENAME);

        if (svgAsString.isEmpty()) {
            throw new IllegalStateException("Failed to provide SVG string due to system errors");
        }
        return svgAsString;
    }
}