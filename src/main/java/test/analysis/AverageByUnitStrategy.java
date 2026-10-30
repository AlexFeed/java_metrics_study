package test.analysis;
import test.model.MetricRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageByUnitStrategy implements MetricStrategy {
    @Override
    public void calculate(List<MetricRecord> records, MetricAnalysisResult result) {
        Map<String, Double> sumByUnit = new HashMap<>();
        Map<String, Integer> countByUnit = new HashMap<>();

        for (MetricRecord record : records) {
            String unit = String.valueOf(record.getUnit());
            sumByUnit.put(unit, sumByUnit.getOrDefault(unit, 0.0) + record.getValue());
            countByUnit.put(unit, countByUnit.getOrDefault(unit, 0) + 1);
        }

        Map<String, Double> avgByUnit = new HashMap<>();
        for (Map.Entry<String, Double> entry : sumByUnit.entrySet()) {
            String unit = entry.getKey();
            avgByUnit.put(unit, entry.getValue() / countByUnit.get(unit));
        }
        result.setAvgByUnit(avgByUnit);
    }
}