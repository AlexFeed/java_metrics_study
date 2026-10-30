package test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MetricRecord implements Serializable {
    private final String metricName;
    private final double value;
    private final MetricUnit unit;
    private final MetricLevel level;

    @JsonCreator
    public MetricRecord(@JsonProperty("name") String metricName,
                        @JsonProperty("value") double value,
                        @JsonProperty("unit") MetricUnit unit,
                        @JsonProperty("level") MetricLevel level) {
        this.metricName = metricName;
        this.value = value;
        this.unit = unit;
        this.level = level;
    }

    public String getMetricName() {
        return metricName;
    }

    public double getValue() {
        return value;
    }

    public MetricUnit getUnit() {
        return unit;
    }

    public MetricLevel getLevel() {
        return level;
    }
}
