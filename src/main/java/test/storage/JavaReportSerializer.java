package test.storage;

import test.analysis.MetricAnalysisResult;

import java.io.*;

public class JavaReportSerializer {
    public static void serialize(MetricAnalysisResult report, String filePath) {
        File file = new File(filePath);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(report);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения сериализованного отчёта в '" + filePath + "': " + e.getMessage(), e);
        }
    }

    public static MetricAnalysisResult deserialize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Файл отчёта не найден: '" + filePath + "'");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof MetricAnalysisResult result) {
                return result;
            } else {
                throw new IllegalStateException("В файле содержится объект неверного типа: " + obj.getClass().getName());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка чтения отчёта из файла '" + filePath + "': " + e.getMessage(), e);
        }
    }
}