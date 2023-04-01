package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Race {
    String name;
    LocalDateTime localDateTime;
    LocalDateTime localDateTimeQualifying;

    public Race(String name, LocalDateTime localDateTime, LocalDateTime localDateTimeQualifying) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.localDateTimeQualifying = localDateTimeQualifying;
    }

    public String getRaceDateAsString() {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
    public String getQualifyingDateAsString() {
        return localDateTimeQualifying.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }
}
