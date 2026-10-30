package test.analysis;
import test.model.MetricRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxByUnitStrategy implements MetricStrategy {
    @Override
    public void calculate(List<MetricRecord> records, MetricAnalysisResult result) {
        Map<String, Double> maxByUnit = new HashMap<>();
        for (MetricRecord record : records) {
            String unit = String.valueOf(record.getUnit());
            double val = record.getValue();

            if (!maxByUnit.containsKey(unit) || val > maxByUnit.get(unit)) {
                maxByUnit.put(unit, val);
            }
        }
        result.setMaxByUnit(maxByUnit);
    }
}