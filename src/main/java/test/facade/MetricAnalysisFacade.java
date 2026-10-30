package test.facade;

import test.analysis.Analyzer;
import test.analysis.MetricAnalysisResult;
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

    /**
     * Основной режим парсинга и анализа с передачей выбранных стратегий
     */
    public void runParseMode(
            InputStream inputStream,
            String jsonPath,
            String reportPath,
            Analyzer<MetricRecord, MetricAnalysisResult> analyzer,
            ReportBuilder<MetricAnalysisResult> reportBuilder) {

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

        // 1. Создание и запись JSON с исходными данными и ошибочными строками
        ParsedResult parsedResult = new ParsedResult(
                "Метрики приложения",
                lineNumber,
                validRecords.size(),
                invalidLines.size(),
                validRecords,
                invalidLines
        );
        jsonWriter.writeToFile(parsedResult, jsonPath);

        // 2. Выполнение расчёта через переданную стратегию анализа
        MetricAnalysisResult result = analyzer.analyze(validRecords);

        // 3. Фасад устанавливает контекст ошибок парсинга
        result.setParseContext(lineNumber, validRecords.size(), invalidLines);

        // 4. Сериализация готового отчета
        JavaReportSerializer.serialize(result, reportPath);

        // 5. Построение отчёта через переданную стратегию отчёта
        String report = reportBuilder.build(result);

        // 6. Вывод в консоль
        System.out.println(report);
    }

    /**
     * Режим отображения сохранённого ранее отчёта
     */
    public void runShowReportMode(
            String filePath,
            ReportBuilder<MetricAnalysisResult> reportBuilder,
            Analyzer<MetricRecord, MetricAnalysisResult> fallbackAnalyzer) {

        MetricAnalysisResult result;

        if (filePath.toLowerCase().endsWith(".json")) {
            // Читаем JSON и пересчитываем статистику
            ParsedResult parsedResult = jsonReader.readFromFile(filePath);
            result = fallbackAnalyzer.analyze(parsedResult.getRecords());
            result.setParseContext(
                    parsedResult.getTotalLines(),
                    parsedResult.getValidLines(),
                    parsedResult.getInvalidRecords()
            );
        } else {
            // Читаем готовый бинарный сериализованный объект
            result = JavaReportSerializer.deserialize(filePath);
        }

        String report = reportBuilder.build(result);
        System.out.println(report);
    }
}