package test.report;

import test.analysis.MetricAnalysisResult;
import test.annotation.StrategyComponent;
import test.model.InvalidLine;
import test.strategy.StrategyType;

import java.util.Locale;
import java.util.Map;

@StrategyComponent(
        name = "detailed",
        group = "metrics",
        type = StrategyType.REPORT
)
public class DetailedMetricReportBuilder implements ReportBuilder<MetricAnalysisResult> {

    @Override
    public String build(MetricAnalysisResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("======================================================================\n");
        sb.append("             РАСШИРЕННЫЙ АНАЛИТИЧЕСКИЙ ОТЧЁТ ПО МЕТРИКАМ              \n");
        sb.append("======================================================================\n");
        sb.append("Конфигурация: Метрики приложения [Формат: Детализированный]\n\n");

        sb.append("----------------------------------------------------------------------\n");
        sb.append("1. Сводные показатели обработки данных\n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append(String.format("%-40s %d\n", "Всего строк во входном потоке:", result.getTotalLines()));

        // Считаем проценты качества данных
        double validPct = result.getTotalLines() > 0 ? (result.getValidLines() * 100.0 / result.getTotalLines()) : 0.0;
        double invalidPct = result.getTotalLines() > 0 ? (result.getInvalidLinesCount() * 100.0 / result.getTotalLines()) : 0.0;

        sb.append(String.format(Locale.US, "%-40s %d (%.1f%%)\n", "Успешно валидировано:", result.getValidLines(), validPct));
        sb.append(String.format(Locale.US, "%-40s %d (%.1f%%)\n", "Ошибочных строк:", result.getInvalidLinesCount(), invalidPct));

        if (!result.hasValidMetrics()) {
            sb.append("\n[Внимание] Валидные метрики отсутствуют. Расчёт статистики невозможен.\n");
            appendErrors(sb, result);
            sb.append("======================================================================\n");
            return sb.toString();
        }

        // 2. Распределение по единицам с расчётом долей (%)
        sb.append("\n----------------------------------------------------------------------\n");
        sb.append("2. Распределение метрик по типам единиц измерения\n");
        sb.append("----------------------------------------------------------------------\n");
        for (Map.Entry<String, Integer> entry : result.getCountByUnit().entrySet()) {
            double percent = (entry.getValue() * 100.0) / result.getValidLines();
            sb.append(String.format(Locale.US, "%-20s %-8d [%5.1f%% от общего объема]\n",
                    entry.getKey(), entry.getValue(), percent));
        }

        // 3. Статус надежности с процентами
        sb.append("\n----------------------------------------------------------------------\n");
        sb.append("3. Статус надежности и уровни важности\n");
        sb.append("----------------------------------------------------------------------\n");
        for (Map.Entry<String, Integer> entry : result.getCountByLevel().entrySet()) {
            double percent = (entry.getValue() * 100.0) / result.getValidLines();
            sb.append(String.format(Locale.US, "%-20s %-8d [%5.1f%%]\n",
                    entry.getKey(), entry.getValue(), percent));
        }
        sb.append(String.format(
                "\nИндекс внимания: %d событий (Предупреждения: %d, Критические: %d)\n",
                result.getAttentionCount(),
                result.getWarningCount(),
                result.getCriticalCount()
        ));

        // 4. Сводная таблица экстремумов и средних
        sb.append("\n----------------------------------------------------------------------\n");
        sb.append("4. Экстремумы и усредненные показатели\n");
        sb.append("----------------------------------------------------------------------\n");
        if (result.getMaxByUnit().isEmpty() && result.getAvgByUnit().isEmpty()) {
            sb.append("Экстремумы не рассчитывались (выбран базовый анализ).\n");
        } else {
            sb.append(String.format("%-15s | %-18s | %-18s\n", "Единица", "Максимум", "Среднее"));
            sb.append("----------------+--------------------+--------------------\n");
            for (String unit : result.getCountByUnit().keySet()) {
                Double max = result.getMaxByUnit().get(unit);
                Double avg = result.getAvgByUnit().get(unit);
                String maxStr = max != null ? String.format(Locale.US, "%.2f", max) : "—";
                String avgStr = avg != null ? String.format(Locale.US, "%.2f", avg) : "—";
                sb.append(String.format("%-15s | %-18s | %-18s\n", unit, maxStr, avgStr));
            }
        }

        appendErrors(sb, result);
        sb.append("======================================================================\n");

        return sb.toString();
    }

    private void appendErrors(StringBuilder sb, MetricAnalysisResult result) {
        sb.append("\n----------------------------------------------------------------------\n");
        sb.append("5. Детализация ошибочных строк\n");
        sb.append("----------------------------------------------------------------------\n");
        if (result.getErrorLines() == null || result.getErrorLines().isEmpty()) {
            sb.append("Ошибочных строк не обнаружено.\n");
        } else {
            for (InvalidLine error : result.getErrorLines()) {
                sb.append(String.format("  [Строка %03d] Содержимое: \"%s\"\n", error.getLineNumber(), error.getLine()));
                sb.append(String.format("               Причина ошибки: %s\n", error.getReason()));
            }
        }
    }
}