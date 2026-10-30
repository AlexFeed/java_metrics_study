package test.analysis;
import test.model.InvalidLine;
import test.model.MetricRecord;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetricAnalysisResult implements Serializable {
    private int totalLines;
    private int validLines;
    private int invalidLinesCount;
    private Map<String, Integer> countByUnit;
    private Map<String, Integer> countByLevel;
    private int warningCount;
    private int criticalCount;
    private Map<String, Double> maxByUnit;
    private Map<String, Double> avgByUnit;
    private List<InvalidLine> errorLines;

    public MetricAnalysisResult() {
        this.countByUnit = Collections.emptyMap();
        this.countByLevel = Collections.emptyMap();
        this.maxByUnit = Collections.emptyMap();
        this.avgByUnit = Collections.emptyMap();
        this.errorLines = Collections.emptyList();
    }

    public void setParseContext(int totalLines, int validLines, List<InvalidLine> errorLines) {
        this.totalLines = totalLines;
        this.validLines = validLines;
        this.errorLines = errorLines != null ? Collections.unmodifiableList(errorLines) : Collections.emptyList();
        this.invalidLinesCount = this.errorLines.size();
    }

    public int getTotalLines() { return totalLines; }
    public int getValidLines() { return validLines; }
    public int getInvalidLinesCount() { return invalidLinesCount; }
    public boolean hasValidMetrics() { return validLines > 0; }
    public Map<String, Integer> getCountByUnit() { return countByUnit; }
    public Map<String, Integer> getCountByLevel() { return countByLevel; }
    public int getWarningCount() { return warningCount; }
    public int getCriticalCount() { return criticalCount; }
    public int getAttentionCount() { return warningCount + criticalCount; }
    public Map<String, Double> getMaxByUnit() { return maxByUnit; }
    public Map<String, Double> getAvgByUnit() { return avgByUnit; }
    public List<InvalidLine> getErrorLines() { return errorLines; }

    public void setCountByUnit(Map<String, Integer> countByUnit) {
        this.countByUnit = Collections.unmodifiableMap(countByUnit);
    }

    public void setCountByLevel(Map<String, Integer> countByLevel) {
        this.countByLevel = Collections.unmodifiableMap(countByLevel);
        this.warningCount = countByLevel.getOrDefault("WARNING", 0);
        this.criticalCount = countByLevel.getOrDefault("CRITICAL", 0);
    }

    public void setMaxByUnit(Map<String, Double> maxByUnit) {
        this.maxByUnit = Collections.unmodifiableMap(maxByUnit);
    }

    public void setAvgByUnit(Map<String, Double> avgByUnit) {
        this.avgByUnit = Collections.unmodifiableMap(avgByUnit);
    }
}