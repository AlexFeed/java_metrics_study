package test.parser;

import test.model.MetricLevel;
import test.model.MetricRecord;
import test.model.MetricUnit;

public class MetricParser {
    public static MetricRecord parse(String line) {
        String[] parts = line.split("\\s+");

        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Неверное количество полей: ожидалось 4, получено " + parts.length);
        }

        String metricName = parts[0];
        if (metricName.isBlank()) {
            throw new IllegalArgumentException("Имя метрики не может быть пустым");
        }

        double value;
        try {
            value = Double.parseDouble(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значение метрики не является числом: '" + parts[1] + "'");
        }

        MetricUnit unit;
        try {
            unit = MetricUnit.valueOf(parts[2].trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимая единица измерения: '" + parts[2] + "'");
        }

        MetricLevel level;
        try {
            level = MetricLevel.valueOf(parts[3].trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимый уровень важности: '" + parts[2] + "'");
        }

        return new MetricRecord(metricName, value, unit, level);
    }
}

