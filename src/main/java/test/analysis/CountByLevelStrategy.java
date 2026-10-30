package test.analysis;
import test.model.MetricRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountByLevelStrategy implements MetricStrategy {
    @Override
    public void calculate(List<MetricRecord> records, MetricAnalysisResult result) {
        Map<String, Integer> countByLevel = new HashMap<>();
        for (MetricRecord record : records) {
            String level = String.valueOf(record.getLevel());
            countByLevel.put(level, countByLevel.getOrDefault(level, 0) + 1);
        }
        result.setCountByLevel(countByLevel);
    }
}
