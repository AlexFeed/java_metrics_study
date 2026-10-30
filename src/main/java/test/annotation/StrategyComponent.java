package test.annotation;

import test.strategy.StrategyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // живет во время работы программы
@Target(ElementType.TYPE)           // можно использовать только на классах
public @interface StrategyComponent {
    String name();         // Имя: "basic", "detailed", "standard"
    String group();        // Группа: "metrics"
    StrategyType type();   // Тип: ANALYZER или REPORT
}