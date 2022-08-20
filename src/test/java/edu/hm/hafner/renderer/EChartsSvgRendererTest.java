package edu.hm.hafner.renderer;

import edu.hm.hafner.util.ResourceTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class EChartsSvgRendererTest extends ResourceTest {

    private String formatStringOutput(String output) {
        return output.replaceAll("\\s+","");
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
            String exportOptions = toString("export-options/export-stack.json");
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
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();

        String exportOptions = toString("export-options/export-stack.json");
        String configOptions = toString("configuration-options/bar-basic.json");
        exportOptions = exportOptions.replace("\n", "");
        configOptions = configOptions.replace("\n", "");

        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);
        result = formatStringOutput(result);

        String isExpectedResult = toString("screenshots/bar-basic.svg");
        isExpectedResult = formatStringOutput(isExpectedResult);

        assertThat(result).isEqualTo(isExpectedResult);
    }

    /**
     * Tests if the application is able to return a stacked bar chart as SVG.
     */
    @Test
    public void shouldRenderStackedBarChart() {
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();

        String exportOptions = toString("export-options/export-basic.json");
        String configOptions = toString("configuration-options/bar-stack.json");
        exportOptions = exportOptions.replace("\n", "");
        configOptions = configOptions.replace("\n", "");

        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);
        result = formatStringOutput(result);

        String isExpectedResult = toString("screenshots/bar-stack.svg");
        isExpectedResult = formatStringOutput(isExpectedResult);

        assertThat(result).isEqualTo(isExpectedResult);
    }
}