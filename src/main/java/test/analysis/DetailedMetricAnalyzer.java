package test.analysis;

import test.annotation.StrategyComponent;
import test.model.MetricRecord;
import test.strategy.StrategyType;

import java.util.List;

@StrategyComponent(
        name = "detailed",
        group = "metrics",
        type = StrategyType.ANALYZER
)
public class DetailedMetricAnalyzer implements Analyzer<MetricRecord, MetricAnalysisResult> {

    // Подключаем полный набор стратегий
    private final List<MetricStrategy> strategies = List.of(
            new CountByUnitStrategy(),
            new CountByLevelStrategy(),
            new MaxByUnitStrategy(),
            new AverageByUnitStrategy()
    );

    @Override
    public MetricAnalysisResult analyze(List<MetricRecord> records) {
        MetricAnalysisResult result = new MetricAnalysisResult();

        // Запускаем полный цикл расчётов
        for (MetricStrategy strategy : strategies) {
            strategy.calculate(records, result);
        }

        return result;
    }
}