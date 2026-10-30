package test.strategy;

public record StrategyKey(
        String group,
        String name,
        StrategyType type
) {
    // Компактный конструктор для проверки валидности данных прямо при создании ключа
    public StrategyKey {
        if (group == null || group.isBlank()) {
            throw new IllegalArgumentException("Группа стратегии не может быть пустой");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя стратегии не может быть пустым");
        }
        if (type == null) {
            throw new IllegalArgumentException("Тип стратегии (StrategyType) не может быть null");
        }
    }
}