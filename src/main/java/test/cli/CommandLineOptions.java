package test.cli;

public class CommandLineOptions {
    private final boolean parseMode;
    private final boolean showReportMode;
    private final boolean help;
    private final String jsonPath;
    private final String reportPath;

    public CommandLineOptions(
            boolean parseMode,
            boolean showReportMode,
            boolean help,
            String jsonPath,
            String reportPath) {
        this.parseMode = parseMode;
        this.showReportMode = showReportMode;
        this.help = help;
        this.jsonPath = jsonPath;
        this.reportPath = reportPath;
    }

    public boolean isParseMode() {
        return parseMode;
    }

    public boolean isShowReportMode() {
        return showReportMode;
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
}