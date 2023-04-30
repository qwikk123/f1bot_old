package org.f1bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Race {
    private final String name;
    private final String circuitName;
    private final LocalDateTime localDateTime;

    private final LocalDateTime localDateTimeQualifying;
    private LocalDateTime localDateTimeSprint;

    public Race(String name,String circuitName, LocalDateTime localDateTime, LocalDateTime localDateTimeQualifying) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.localDateTimeQualifying = localDateTimeQualifying;
        this.circuitName = circuitName;
    }


    public String getRaceDateAsString() {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
    public String getQualifyingDateAsString() {
        return localDateTimeQualifying.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
    public String getSprintDateAsString() {
        return localDateTimeSprint.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
    public String getName() { return name; }
    public String getCircuitName() { return circuitName; }
    public LocalDateTime getLocalDateTime() { return localDateTime; }
    public void setSprint(LocalDateTime localDateTimeSprint) {
        this.localDateTimeSprint = localDateTimeSprint;
    }
    public boolean hasSprint() {
        return localDateTimeSprint != null;
    }
}
