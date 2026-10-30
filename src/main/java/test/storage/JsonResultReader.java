package test.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.model.ParsedResult;

import java.io.File;
import java.io.IOException;

public class JsonResultReader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ParsedResult readFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("JSON-файл не найден: " + filePath);
        }

        try {
            return objectMapper.readValue(file, ParsedResult.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения JSON-файла '" + filePath + "': " + e.getMessage(), e);
        }
    }
}