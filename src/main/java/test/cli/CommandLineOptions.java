package test.cli;

public class CommandLineOptions {
    private final boolean parseMode;
    private final boolean showReportMode;
    private final boolean listStrategies;
    private final boolean help;
    private final String jsonPath;
    private final String reportPath;
    private final String group;
    private final String analysis;
    private final String reportFormat;

    public CommandLineOptions(
            boolean parseMode,
            boolean showReportMode,
            boolean listStrategies,
            boolean help,
            String jsonPath,
            String reportPath,
            String group,
            String analysis,
            String reportFormat) {
        this.parseMode = parseMode;
        this.showReportMode = showReportMode;
        this.listStrategies = listStrategies;
        this.help = help;
        this.jsonPath = jsonPath;
        this.reportPath = reportPath;
        this.group = group;
        this.analysis = analysis;
        this.reportFormat = reportFormat;
    }

    public boolean isParseMode() {
        return parseMode;
    }

    public boolean isShowReportMode() {
        return showReportMode;
    }

    public boolean isListStrategies() {
        return listStrategies;
    }

    public boolean isHelp() {
        return help;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public String getGroup() {
        return group;
    }

    public String getAnalysis() {
        return analysis;
    }

    public String getReportFormat() {
        return reportFormat;
    }
}