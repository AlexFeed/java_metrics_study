package test.strategy;

import test.analysis.Analyzer;
import test.annotation.StrategyComponent;
import test.report.ReportBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

public class StrategyRegistry {

    // Главное хранилище: Ключ -> Готовый созданный экземпляр стратегии
    private final Map<StrategyKey, Object> strategies = new HashMap<>();

    /**
     * Регистрация класса стратегии по правилам Рефлексии
     */
    public void register(Class<?> clazz) {
        // 1. Проверяем наличие аннотации @StrategyComponent
        if (!clazz.isAnnotationPresent(StrategyComponent.class)) {
            throw new IllegalArgumentException("Класс " + clazz.getName() + " не помечен аннотацией @StrategyComponent");
        }

        // 2. Читаем аннотацию через Рефлексию
        StrategyComponent meta = clazz.getAnnotation(StrategyComponent.class);
        String group = meta.group().trim().toLowerCase();
        String name = meta.name().trim().toLowerCase();
        StrategyType type = meta.type();

        StrategyKey key = new StrategyKey(group, name, type);

        // 3. Проверка на ДУБЛИКАТЫ (требование раздела 9 ТЗ)
        if (strategies.containsKey(key)) {
            Object existing = strategies.get(key);
            throw new IllegalStateException(String.format(
                    "Обнаружен дубликат стратегии! Ключ [%s:%s:%s] уже зарегистрирован классом %s. Попытка зарегистрировать: %s",
                    group, name, type, existing.getClass().getName(), clazz.getName()
            ));
        }

        // 4. Проверка: класс не должен быть абстрактным или интерфейсом
        int modifiers = clazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            throw new IllegalArgumentException("Класс стратегии не может быть абстрактным или интерфейсом: " + clazz.getName());
        }

        // 5. Проверка соответствия интерфейсам (требование раздела 9 ТЗ)
        if (type == StrategyType.ANALYZER && !Analyzer.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(String.format(
                    "Класс %s объявлен как ANALYZER, но не реализует интерфейс %s",
                    clazz.getName(), Analyzer.class.getName()
            ));
        }
        if (type == StrategyType.REPORT && !ReportBuilder.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(String.format(
                    "Класс %s объявлен как REPORT, но не реализует интерфейс %s",
                    clazz.getName(), ReportBuilder.class.getName()
            ));
        }

        // 6. Создание экземпляра через Рефлексию (поиск конструктора по умолчанию)
        Object instance;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            instance = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("У класса " + clazz.getName() + " отсутствует публичный конструктор без параметров", e);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать экземпляр класса " + clazz.getName() + " через рефлексию: " + e.getMessage(), e);
        }

        // 7. Сохраняем в мапу
        strategies.put(key, instance);
    }

    /**
     * Поиск стратегии Analyzer с проверкой типов.
     */
    @SuppressWarnings("unchecked")
    public <T, R> Analyzer<T, R> getAnalyzer(String group, String name) {
        StrategyKey key = new StrategyKey(group.toLowerCase(), name.toLowerCase(), StrategyType.ANALYZER);
        Object strategy = strategies.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException(String.format(
                    "Стратегия анализа не найдена для группы '%s' и имени '%s'. Доступные стратегии можно увидеть через --list-strategies",
                    group, name
            ));
        }
        return (Analyzer<T, R>) strategy;
    }

    /**
     * Поиск стратегии ReportBuilder с проверкой типов.
     */
    @SuppressWarnings("unchecked")
    public <R> ReportBuilder<R> getReportBuilder(String group, String name) {
        StrategyKey key = new StrategyKey(group.toLowerCase(), name.toLowerCase(), StrategyType.REPORT);
        Object strategy = strategies.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException(String.format(
                    "Стратегия отчёта не найдена для группы '%s' и имени '%s'. Доступные стратегии можно увидеть через --list-strategies",
                    group, name
            ));
        }
        return (ReportBuilder<R>) strategy;
    }

    /**
     * Формирование красивого вывода для режима CLI: --list-strategies (раздел 12 ТЗ).
     */
    public String getFormattedStrategyList() {
        if (strategies.isEmpty()) {
            return "Зарегистрированных стратегий нет.\n";
        }

        // Группируем стратегии: Группа -> (Тип -> Список имён)
        Map<String, Map<StrategyType, List<String>>> grouped = new TreeMap<>();

        for (StrategyKey key : strategies.keySet()) {
            grouped.computeIfAbsent(key.group(), g -> new EnumMap<>(StrategyType.class))
                    .computeIfAbsent(key.type(), t -> new ArrayList<>())
                    .add(key.name());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Доступные стратегии\n\n");

        for (Map.Entry<String, Map<StrategyType, List<String>>> groupEntry : grouped.entrySet()) {
            sb.append("Группа: ").append(groupEntry.getKey()).append("\n");

            Map<StrategyType, List<String>> byType = groupEntry.getValue();

            // 1. Выводим анализаторы
            sb.append("  ").append(StrategyType.ANALYZER).append(":\n");
            List<String> analyzers = byType.getOrDefault(StrategyType.ANALYZER, Collections.emptyList());
            Collections.sort(analyzers);
            for (String a : analyzers) {
                sb.append("    ").append(a).append("\n");
            }

            // 2. Выводим построители отчётов
            sb.append("  ").append(StrategyType.REPORT).append(":\n");
            List<String> reports = byType.getOrDefault(StrategyType.REPORT, Collections.emptyList());
            Collections.sort(reports);
            for (String r : reports) {
                sb.append("    ").append(r).append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}