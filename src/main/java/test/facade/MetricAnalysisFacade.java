package test.facade;

import test.analysis.MetricAnalysisResult;
import test.analysis.MetricAnalyzer;
import test.model.InvalidLine;
import test.model.MetricRecord;
import test.model.ParsedResult;
import test.parser.MetricParser;
import test.report.ReportBuilder;
import test.storage.JavaReportSerializer;
import test.storage.JsonResultReader;
import test.storage.JsonResultWriter;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MetricAnalysisFacade {
    private final JsonResultWriter jsonWriter = new JsonResultWriter();
    private final JsonResultReader jsonReader = new JsonResultReader();

    public void runParseMode(InputStream inputStream, String jsonPath, String reportPath) {
        List<MetricRecord> validRecords = new ArrayList<>();
        List<InvalidLine> invalidLines = new ArrayList<>();
        int lineNumber = 0;

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();

                if (line.isBlank()) {
                    continue;
                }

                try {
                    MetricRecord record = MetricParser.parse(line);
                    validRecords.add(record);
                } catch (IllegalArgumentException e) {
                    invalidLines.add(new InvalidLine(lineNumber, line, e.getMessage()));
                }
            }
        }

//      Создание объекта результата парсинга для json сериализации
        ParsedResult parsedResult = new ParsedResult(
                "Метрики приложения",
                lineNumber,
                validRecords.size(),
                invalidLines.size(),
                validRecords,
                invalidLines
        );

//      Запись объекта в json
        jsonWriter.writeToFile(parsedResult, jsonPath);

        MetricAnalysisResult result = MetricAnalyzer.analyze(validRecords);

//      Сериализация готового отчета
        JavaReportSerializer.serialize(result, reportPath);

        String report = ReportBuilder.build(result);

        System.out.println(report);
    }

    public void runShowReportMode(String filePath) {
        MetricAnalysisResult result;

        if (filePath.toLowerCase().endsWith(".json")) {
            // Читаем JSON и заново считаем статистику
            ParsedResult parsedResult = jsonReader.readFromFile(filePath);
            result = MetricAnalyzer.analyze(
                    parsedResult.getTotalLines(),
                    parsedResult.getRecords(),
                    parsedResult.getInvalidRecords()
            );
        } else {
            // Читаем бинарный сериализованный объект
            result = JavaReportSerializer.deserialize(filePath);
        }

        String report = ReportBuilder.build(result);
        System.out.println(report);
    }
}