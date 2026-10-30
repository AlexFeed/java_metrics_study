package test.analysis;

import java.util.List;

public interface Analyzer<T, R> {
    R analyze(List<T> records);
}