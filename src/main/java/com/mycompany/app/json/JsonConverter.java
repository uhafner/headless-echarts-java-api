package com.mycompany.app.json;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Converts a Configuration object to a JSON file.
 */
public class JsonConverter {
    Configuration config = new Configuration(800, 600);

    /**
     * Create a JSON file from a Configuration object.
     */
    public void createJson() {
        final ObjectMapper mapper = new ObjectMapper();
        File configJson = new File("target/config.json");

        try {
            mapper.writeValue(configJson, config);
        } catch (IOException e) {
            System.out.println("Unable to build json file.");
            e.printStackTrace();
        }
    }
}
