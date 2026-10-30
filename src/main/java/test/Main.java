package test;

import test.analysis.Analyzer;
import test.analysis.MetricAnalysisResult;
import test.cli.CommandLineOptions;
import test.cli.CommandLineParser;
import test.facade.MetricAnalysisFacade;
import test.model.MetricRecord;
import test.report.ReportBuilder;
import test.strategy.StrategyRegistry;
import test.strategy.StrategyScanner;

public class Main {
  public static void main(String[] args) {
    try {
      // 1. Парсинг параметров командной строки
      CommandLineOptions options = CommandLineParser.parse(args);

      if (options.isHelp()) {
        CommandLineParser.printHelp();
        return;
      }

      // 2. Автоматическое сканирование classpath и регистрация стратегий через Reflection
      StrategyRegistry registry = new StrategyRegistry();
      StrategyScanner scanner = new StrategyScanner();
      scanner.scanAndRegister(registry);

      // 3. Самостоятельный режим вывода списка стратегий
      if (options.isListStrategies()) {
        System.out.println(registry.getFormattedStrategyList());
        return;
      }

      MetricAnalysisFacade facade = new MetricAnalysisFacade();

      // 4. Режим анализа потока данных
      if (options.isParseMode()) {
        // Извлекаем выбранные стратегии из реестра по группе и имени
        Analyzer<MetricRecord, MetricAnalysisResult> analyzer =
                registry.getAnalyzer(options.getGroup(), options.getAnalysis());

        ReportBuilder<MetricAnalysisResult> reportBuilder =
                registry.getReportBuilder(options.getGroup(), options.getReportFormat());

        facade.runParseMode(
                System.in,
                options.getJsonPath(),
                options.getReportPath(),
                analyzer,
                reportBuilder
        );

        // 5. Режим отображения существующего отчёта
      } else if (options.isShowReportMode()) {
        String targetPath = options.getReportPath() != null
                ? options.getReportPath()
                : options.getJsonPath();

        // Для показа отчёта берем указанный формат либо стандартный по умолчанию
        String group = options.getGroup() != null ? options.getGroup() : "metrics";
        String formatName = options.getReportFormat() != null ? options.getReportFormat() : "standard";

        ReportBuilder<MetricAnalysisResult> reportBuilder = registry.getReportBuilder(group, formatName);
        Analyzer<MetricRecord, MetricAnalysisResult> analyzer = registry.getAnalyzer(group, "basic");

        facade.runShowReportMode(targetPath, reportBuilder, analyzer);
      }

    } catch (IllegalArgumentException | IllegalStateException e) {
      System.err.println("Ошибка: " + e.getMessage());
      System.err.println();
      CommandLineParser.printHelp();
    } catch (Exception e) {
      System.err.println("Критическая ошибка выполнения: " + e.getMessage());
      e.printStackTrace();
    }
  }
}