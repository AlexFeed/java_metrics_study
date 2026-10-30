package test.analysis;

import test.annotation.StrategyComponent;
import test.model.MetricRecord;
import test.strategy.StrategyType;

import java.util.List;

@StrategyComponent(
        name = "basic",
        group = "metrics",
        type = StrategyType.ANALYZER
)
public class BasicMetricAnalyzer implements Analyzer<MetricRecord, MetricAnalysisResult> {

    // Подключаем только подсчёт количеств
    private final List<MetricStrategy> strategies = List.of(
            new CountByUnitStrategy(),
            new CountByLevelStrategy()
    );

    @Override
    public MetricAnalysisResult analyze(List<MetricRecord> records) {
        MetricAnalysisResult result = new MetricAnalysisResult();

        // Запускаем только базовые стратегии
        for (MetricStrategy strategy : strategies) {
            strategy.calculate(records, result);
        }

        return result;
    }
}