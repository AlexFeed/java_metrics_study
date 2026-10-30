package test.report;

import test.analysis.MetricAnalysisResult;
import test.annotation.StrategyComponent;
import test.model.InvalidLine;
import test.strategy.StrategyType;

import java.util.Locale;
import java.util.Map;

@StrategyComponent(
        name = "standard",
        group = "metrics",
        type = StrategyType.REPORT
)
public class StandardMetricReportBuilder implements ReportBuilder<MetricAnalysisResult> {

    @Override
    public String build(MetricAnalysisResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("============================================================\n");
        sb.append("                    ОТЧЁТ ПО МЕТРИКАМ                       \n");
        sb.append("============================================================\n");
        sb.append("Вариант: Метрики приложения [Формат: Стандартный]\n\n");

        sb.append("------------------------------------------------------------\n");
        sb.append("1. Общая информация\n");
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("%-35s %d\n", "Всего строк обработано:", result.getTotalLines()));
        sb.append(String.format("%-35s %d\n", "Корректных записей:", result.getValidLines()));
        sb.append(String.format("%-35s %d\n", "Некорректных строк:", result.getInvalidLinesCount()));

        // Если валидных записей не было вообще — выводим только ошибки
        if (!result.hasValidMetrics()) {
            sb.append("\nНет корректных данных для формирования детальной статистики.\n");
            appendErrors(sb, result);
            sb.append("============================================================\n");
            return sb.toString();
        }

        // 2. Распределение по единицам измерения
        sb.append("\n------------------------------------------------------------\n");
        sb.append("2. Метрики по единицам измерения\n");
        sb.append("------------------------------------------------------------\n");
        for (Map.Entry<String, Integer> entry : result.getCountByUnit().entrySet()) {
            sb.append(String.format("%-20s %d\n", entry.getKey(), entry.getValue()));
        }

        // 3. Распределение по уровням важности
        sb.append("\n------------------------------------------------------------\n");
        sb.append("3. Метрики по уровням важности\n");
        sb.append("------------------------------------------------------------\n");
        for (Map.Entry<String, Integer> entry : result.getCountByLevel().entrySet()) {
            sb.append(String.format("%-20s %d\n", entry.getKey(), entry.getValue()));
        }
        sb.append(String.format(
                "\n%-35s %d (WARNING: %d, CRITICAL: %d)\n",
                "Всего требующих внимания:",
                result.getAttentionCount(),
                result.getWarningCount(),
                result.getCriticalCount()
        ));

        // 4. Максимумы (выводим только если они были рассчитаны!)
        if (result.getMaxByUnit() != null && !result.getMaxByUnit().isEmpty()) {
            sb.append("\n------------------------------------------------------------\n");
            sb.append("4. Максимальные значения по единицам измерения\n");
            sb.append("------------------------------------------------------------\n");
            for (Map.Entry<String, Double> entry : result.getMaxByUnit().entrySet()) {
                sb.append(String.format(Locale.US, "%-20s %.2f %s\n", entry.getKey() + ":", entry.getValue(), entry.getKey()));
            }
        }

        // 5. Средние значения (выводим только если они были рассчитаны!)
        if (result.getAvgByUnit() != null && !result.getAvgByUnit().isEmpty()) {
            sb.append("\n------------------------------------------------------------\n");
            sb.append("5. Средние значения по единицам измерения\n");
            sb.append("------------------------------------------------------------\n");
            for (Map.Entry<String, Double> entry : result.getAvgByUnit().entrySet()) {
                sb.append(String.format(Locale.US, "%-20s %.2f %s\n", entry.getKey() + ":", entry.getValue(), entry.getKey()));
            }
        }

        appendErrors(sb, result);
        sb.append("============================================================\n");

        return sb.toString();
    }

    private void appendErrors(StringBuilder sb, MetricAnalysisResult result) {
        sb.append("\n------------------------------------------------------------\n");
        sb.append("6. Некорректные строки\n");
        sb.append("------------------------------------------------------------\n");
        if (result.getErrorLines() == null || result.getErrorLines().isEmpty()) {
            sb.append("Некорректных строк нет.\n");
        } else {
            for (InvalidLine error : result.getErrorLines()) {
                sb.append("Строчка ").append(error.getLineNumber()).append(": ").append(error.getLine()).append("\n");
                sb.append("Причина: ").append(error.getReason()).append("\n");
            }
        }
    }
}