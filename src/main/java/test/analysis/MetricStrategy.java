package test.analysis;

import test.analysis.MetricAnalysisResult;
import test.model.MetricRecord;
import java.util.List;

public interface MetricStrategy {
    void calculate(List<MetricRecord> records, MetricAnalysisResult result);
}