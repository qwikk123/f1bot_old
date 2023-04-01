package org.f1bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Race {
    String name;
    String circuitName;
    LocalDateTime localDateTime;
    LocalDateTime localDateTimeQualifying;

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
}
