package test.strategy;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import test.annotation.StrategyComponent;

import java.util.ArrayList;
import java.util.List;

public class StrategyScanner {

    private final String packageToScan;

    // По умолчанию сканируем наш корневой пакет "test"
    public StrategyScanner() {
        this.packageToScan = "test";
    }

    public StrategyScanner(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    /**
     * Автоматически находит все классы с аннотацией @StrategyComponent
     * и регистрирует их в переданном реестре.
     */
    public void scanAndRegister(StrategyRegistry registry) {
        List<Class<?>> foundClasses = scan();

        for (Class<?> clazz : foundClasses) {
            registry.register(clazz);
        }
    }

    /**
     * Сканирование classpath через ClassGraph.
     * Возвращает список найденных классов со стикером @StrategyComponent.
     */
    public List<Class<?>> scan() {
        List<Class<?>> classes = new ArrayList<>();

        // Запуск сканирования пакета
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageToScan)
                .scan()) {

            // Ищем классы, помеченные нашей аннотацией
            ClassInfoList annotated = scanResult.getClassesWithAnnotation(StrategyComponent.class.getName());

            for (ClassInfo classInfo : annotated) {
                // Загружаем класс в память JVM
                Class<?> clazz = classInfo.loadClass();
                classes.add(clazz);
            }
        }

        return classes;
    }
}