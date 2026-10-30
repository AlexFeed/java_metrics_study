package test.report;

public interface ReportBuilder<R> {
    String build(R result);
}