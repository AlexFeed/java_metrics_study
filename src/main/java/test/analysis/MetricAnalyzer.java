package test.analysis;
import test.model.InvalidLine;
import test.model.MetricRecord;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetricAnalyzer {
    private static final List<MetricStrategy> STRATEGIES = List.of(
            new CountByUnitStrategy(),
            new CountByLevelStrategy(),
            new MaxByUnitStrategy(),
            new AverageByUnitStrategy()
    );

    public static MetricAnalysisResult analyze(List<MetricRecord> validMetrics) {
        if (validMetrics.isEmpty()) {
            return new MetricAnalysisResult();
        }

        MetricAnalysisResult result = new MetricAnalysisResult();

        for (MetricStrategy strategy : STRATEGIES) {
            strategy.calculate(validMetrics, result);
        }

        return result;
    }
}