package test.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class InvalidLine implements Serializable {
    private final int lineNumber;
    private final String line;
    private final String reason;

    @JsonCreator
    public InvalidLine(
            @JsonProperty("lineNumber") int lineNumber,
            @JsonProperty("line") String line,
            @JsonProperty("reason") String reason) {
        this.lineNumber = lineNumber;
        this.line = line;
        this.reason = reason;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() {
        return line;
    }

    public String getReason() {
        return reason;
    }
}
