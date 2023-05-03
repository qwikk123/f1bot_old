package org.f1bot.f1data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Race {
    private final String name;
    private final String circuitName;
    private final LocalDateTime localDateTime;
    private final LocalDateTime localDateTimeQualifying;
    private LocalDateTime localDateTimeSprint;
    private final int round;

    public Race(String name,String circuitName, LocalDateTime localDateTime, LocalDateTime localDateTimeQualifying, int round) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.localDateTimeQualifying = localDateTimeQualifying;
        this.circuitName = circuitName;
        this.round = round;
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
    public int getRound() { return round; }
    public String getCircuitName() { return circuitName; }
    public LocalDateTime getLocalDateTime() { return localDateTime; }
    public void setSprint(LocalDateTime localDateTimeSprint) {
        this.localDateTimeSprint = localDateTimeSprint;
    }
    public boolean hasSprint() {
        return localDateTimeSprint != null;
    }
    public String getImagePath() {
        return "assets/circuitimages/"+circuitName.replaceAll(" ","")+".png";
    }
    public String getImageName() {
        return circuitName.replaceAll(" ","")+".png";
    }
}
