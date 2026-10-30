package test.analysis;
import test.model.MetricRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountByUnitStrategy implements MetricStrategy {
    @Override
    public void calculate(List<MetricRecord> records, MetricAnalysisResult result) {
        Map<String, Integer> counts = new HashMap<>();
        for (MetricRecord record : records) {
            String unit = String.valueOf(record.getUnit());
            counts.put(unit, counts.getOrDefault(unit, 0) + 1);
        }
        result.setCountByUnit(counts);
    }
}
