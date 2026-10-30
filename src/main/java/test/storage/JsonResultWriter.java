package test.storage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import test.model.ParsedResult;

import java.io.File;
import java.io.IOException;

public class JsonResultWriter {
    private final ObjectMapper objectMapper;

    public JsonResultWriter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void writeToFile(ParsedResult result, String filePath) {
        try {
            File file = new File(filePath);
            objectMapper.writeValue(file, result);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении JSON в файл '" + filePath + "': " + e.getMessage(), e);
        }
    }
}