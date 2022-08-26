package edu.hm.hafner.renderer;

import edu.hm.hafner.util.ResourceTest;

import org.junit.jupiter.api.Test;

import org.xmlunit.builder.DiffBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class EChartsSvgRendererTest extends ResourceTest {

    /**
     * Converts a SVG provided by its file path to convert it to a string.
     * @param filePath File path of SVG string
     * @return SVG as string
     */
    private String convertSvgToString(String filePath) {
        String svgString = toString(filePath);

        if (!svgString.isEmpty()) {
            return svgString.replace("\n", "");
        } else {
            return "";
        }
    }

    /**
     * Compares two SVG strings if they are equal, while ignoring comments and whitespaces.
     * @param testResultSvg SVG string produced by EChartsSvgRenderer
     * @param expectedSvg SVG string equivalent to test result SVG
     * @return Returns true if SVG strings are equal.
     */
    private boolean compareSvgs(String testResultSvg, String expectedSvg) {
        return DiffBuilder.compare(testResultSvg).withTest(expectedSvg)
                .ignoreComments()
                .ignoreWhitespace()
                .build()
                .hasDifferences();
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if no parameters were provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfAllParamsAreEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString("", "");
        });
        String expectedMessage = "No parameters were provided";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if no chart config parameter was provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfChartConfigIsEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            String exportOptions = toString("export-options/export-medium.json");
            echartsSvgRenderer.createSvgString("", exportOptions);
        });
        String expectedMessage = "Chart configuration options parameter is missing.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if no export config parameter was provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfExportConfigIsEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            String configOptions = toString("configuration-options/bar-basic.json");
            echartsSvgRenderer.createSvgString(configOptions, "");
        });
        String expectedMessage = "Chart export options parameter is missing.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application is able to return a basic bar chart as SVG.
     */
    @Test
    public void shouldRenderBasicBarChart() {
        String exportOptions = convertSvgToString("export-options/export-medium.json");
        String configOptions = convertSvgToString("configuration-options/bar-basic.json");
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);

        String isExpectedResult = toString("screenshots/bar-basic.svg");
        assertThat(compareSvgs(result, isExpectedResult)).isFalse();
    }

    /**
     * Tests if the application is able to return a stacked bar chart as SVG.
     */
    @Test
    public void shouldRenderStackedBarChart() {
        String exportOptions = convertSvgToString("export-options/export-large.json");
        String configOptions = convertSvgToString("configuration-options/bar-stack.json");
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);

        String isExpectedResult = toString("screenshots/bar-stack.svg");
        assertThat(compareSvgs(result, isExpectedResult)).isFalse();
    }
}