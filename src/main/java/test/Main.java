package test;

import test.cli.CommandLineOptions;
import test.cli.CommandLineParser;
import test.facade.MetricAnalysisFacade;

public class Main {
  public static void main(String[] args) {
    try {
      CommandLineOptions options = CommandLineParser.parse(args);

      if (options.isHelp()) {
        CommandLineParser.printHelp();
        return;
      }

      MetricAnalysisFacade facade = new MetricAnalysisFacade();

      if (options.isParseMode()) {
        facade.runParseMode(System.in, options.getJsonPath(), options.getReportPath());
      } else if (options.isShowReportMode()) {
        String targetPath = options.getReportPath() != null
                ? options.getReportPath()
                : options.getJsonPath();

        facade.runShowReportMode(targetPath);
      }

    } catch (IllegalArgumentException e) {
      System.err.println("Ошибка параметров запуска: " + e.getMessage());
      System.err.println();
      CommandLineParser.printHelp();
    }
  }
}