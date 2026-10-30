package test.cli;

public class CommandLineParser {

    public static CommandLineOptions parse(String[] args) {
        boolean parseMode = false;
        boolean showReportMode = false;
        boolean help = false;
        String jsonPath = null;
        String reportPath = null;

        int i = 0;
        while (i < args.length) {
            String arg = args[i];

            switch (arg) {
                case "--help":
                    help = true;
                    break;
                case "--parse":
                    parseMode = true;
                    break;
                case "--show-report":
                    showReportMode = true;
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
            return new CommandLineOptions(false, false, true, null, null);
        }

        if (!parseMode && !showReportMode) {
            throw new IllegalArgumentException("Не указан режим запуска (--parse или --show-report).");
        }

        if (parseMode && showReportMode) {
            throw new IllegalArgumentException("Нельзя одновременно указывать режимы --parse и --show-report.");
        }

        if (parseMode) {
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

        return new CommandLineOptions(parseMode, showReportMode, false, jsonPath, reportPath);
    }

    public static void printHelp() {
        System.out.println("Использование:");
        System.out.println();
        System.out.println("1. Проанализировать входные данные:");
        System.out.println("   java Main --parse --json parsed-result.json --report report.ser < input.txt");
        System.out.println();
        System.out.println("2. Вывести сохранённый отчёт:");
        System.out.println("   java Main --show-report --report report.ser");
        System.out.println();
        System.out.println("Параметры:");
        System.out.println("  --parse          прочитать входной поток, построить отчёт и сохранить файлы");
        System.out.println("  --show-report    прочитать сохранённый сериализованный отчёт и вывести его");
        System.out.println("  --json <file>    путь к JSON-файлу для сохранения распарсенных данных");
        System.out.println("  --report <file>  путь к файлу сериализованного отчёта");
        System.out.println("  --help           показать подсказку");
    }
}