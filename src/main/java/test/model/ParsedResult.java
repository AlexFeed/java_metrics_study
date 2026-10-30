package test.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ParsedResult {
    private final String variant;
    private final int totalLines;
    private final int validLines;
    private final int invalidLines;
    private final List<MetricRecord> records;
    private final List<InvalidLine> invalidRecords;

    @JsonCreator
    public ParsedResult(
            @JsonProperty("variant") String variant,
            @JsonProperty("totalLines") int totalLines,
            @JsonProperty("validLines") int validLines,
            @JsonProperty("invalidLines") int invalidLines,
            @JsonProperty("records") List<MetricRecord> records,
            @JsonProperty("invalidRecords") List<InvalidLine> invalidRecords) {
        this.variant = variant;
        this.totalLines = totalLines;
        this.validLines = validLines;
        this.invalidLines = invalidLines;
        this.records = records;
        this.invalidRecords = invalidRecords;
    }

    public String getVariant() { return variant; }
    public int getTotalLines() { return totalLines; }
    public int getValidLines() { return validLines; }
    public int getInvalidLines() { return invalidLines; }
    public List<MetricRecord> getRecords() { return records; }
    public List<InvalidLine> getInvalidRecords() { return invalidRecords; }
}