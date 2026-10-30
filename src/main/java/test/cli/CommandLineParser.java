package test.cli;

public class CommandLineParser {

    public static CommandLineOptions parse(String[] args) {
        boolean parseMode = false;
        boolean showReportMode = false;
        boolean listStrategies = false;
        boolean help = false;

        String jsonPath = null;
        String reportPath = null;
        String group = null;
        String analysis = null;
        String reportFormat = null;

        int i = 0;
        while (i < args.length) {
            String arg = args[i];

            switch (arg) {
                case "--help":
                    help = true;
                    break;
                case "--list-strategies":
                    listStrategies = true;
                    break;
                case "--parse":
                    parseMode = true;
                    break;
                case "--show-report":
                    showReportMode = true;
                    break;
                case "--group":
                    if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                        throw new IllegalArgumentException("После параметра --group не указано имя группы.");
                    }
                    group = args[++i];
                    break;
                case "--analysis":
                    if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                        throw new IllegalArgumentException("После параметра --analysis не указано имя стратегии анализа.");
                    }
                    analysis = args[++i];
                    break;
                case "--report-format":
                    if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                        throw new IllegalArgumentException("После параметра --report-format не указано имя формата отчёта.");
                    }
                    reportFormat = args[++i];
                    break;
                case "--json":
                    if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                        throw new IllegalArgumentException("После параметра --json не указан путь к файлу.");
                    }
                    jsonPath = args[++i];
                    break;
                case "--report":
                    if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                        throw new IllegalArgumentException("После параметра --report не указан путь к файлу.");
                    }
                    reportPath = args[++i];
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестный параметр: " + arg);
            }
            i++;
        }

        if (help) {
            return new CommandLineOptions(false, false, false, true, null, null, null, null, null);
        }

        // Проверка автономного режима --list-strategies
        if (listStrategies) {
            if (parseMode || showReportMode) {
                throw new IllegalArgumentException("Режим --list-strategies является самостоятельным и не может сочетаться с --parse или --show-report.");
            }
            return new CommandLineOptions(false, false, true, false, null, null, null, null, null);
        }

        if (!parseMode && !showReportMode) {
            throw new IllegalArgumentException("Не указан режим запуска (--parse, --show-report или --list-strategies).");
        }

        if (parseMode && showReportMode) {
            throw new IllegalArgumentException("Нельзя одновременно указывать режимы --parse и --show-report.");
        }

        if (parseMode) {
            if (group == null || group.isBlank()) {
                throw new IllegalArgumentException("Для режима --parse обязательно укажите целевую группу: --group <name>");
            }
            if (analysis == null || analysis.isBlank()) {
                throw new IllegalArgumentException("Для режима --parse обязательно укажите стратегию анализа: --analysis <name>");
            }
            if (reportFormat == null || reportFormat.isBlank()) {
                throw new IllegalArgumentException("Для режима --parse обязательно укажите формат отчёта: --report-format <name>");
            }
            if (jsonPath == null || jsonPath.isBlank()) {
                throw new IllegalArgumentException("Для режима --parse обязательно укажите путь к JSON-файлу: --json <file>");
            }
            if (reportPath == null || reportPath.isBlank()) {
                throw new IllegalArgumentException("Для режима --parse обязательно укажите путь к файлу отчёта: --report <file>");
            }
        }

        if (showReportMode) {
            boolean hasReport = reportPath != null && !reportPath.isBlank();
            boolean hasJson = jsonPath != null && !jsonPath.isBlank();

            if (!hasReport && !hasJson) {
                throw new IllegalArgumentException("Для режима --show-report укажите файл отчёта: --report <file.ser|file.json> или --json <file.json>");
            }
        }

        return new CommandLineOptions(
                parseMode,
                showReportMode,
                false,
                false,
                jsonPath,
                reportPath,
                group,
                analysis,
                reportFormat
        );
    }

    public static void printHelp() {
        System.out.println("Использование:");
        System.out.println();
        System.out.println("1. Просмотр доступных стратегий:");
        System.out.println("   java Main --list-strategies");
        System.out.println();
        System.out.println("2. Проанализировать входные данные с выбором стратегий:");
        System.out.println("   java Main --parse --group metrics --analysis basic --report-format standard \\");
        System.out.println("             --json parsed-result.json --report report.ser < input.txt");
        System.out.println();
        System.out.println("3. Вывести сохранённый отчёт:");
        System.out.println("   java Main --show-report --report report.ser");
        System.out.println();
        System.out.println("Параметры:");
        System.out.println("  --list-strategies        показать список зарегистрированных стратегий");
        System.out.println("  --parse                  прочитать входной поток, проанализировать и сохранить результат");
        System.out.println("  --show-report            прочитать сохранённый сериализованный отчёт и вывести его");
        System.out.println("  --group <name>           группа стратегий (например: metrics)");
        System.out.println("  --analysis <name>        имя стратегии анализатора (например: basic, detailed)");
        System.out.println("  --report-format <name>   имя стратегии отчёта (например: standard, detailed)");
        System.out.println("  --json <file>            путь к JSON-файлу с результатами парсинга");
        System.out.println("  --report <file>          путь к файлу бинарного отчёта (.ser)");
        System.out.println("  --help                   показать справку");
    }
}