package qwikk.f1bot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents an F1 race.
 */
public class Race {
    private final String name;
    private final String circuitName;
    private final LocalDateTime localDateTime;
    private final LocalDateTime localDateTimeQualifying;
    private LocalDateTime localDateTimeSprint;
    private final int round;
    private RaceResult raceResult;

    /**
     * Creates an instance of Race representing an F1 Race
     * @param name name of the race
     * @param circuitName name of the circuit the race is using
     * @param localDateTime the datetime for when the race starts
     * @param localDateTimeQualifying the datetime for when the qualifying starts
     * @param round the race position in the calendar
     */
    public Race(String name,String circuitName, LocalDateTime localDateTime, LocalDateTime localDateTimeQualifying, int round) {
        this.name = name;
        this.localDateTime = localDateTime;
        this.localDateTimeQualifying = localDateTimeQualifying;
        this.circuitName = circuitName;
        this.round = round;
    }


    public void setRaceResult(RaceResult raceResult) { this.raceResult = raceResult; }
    public RaceResult getRaceResult() { return raceResult; }
    public boolean hasRaceResult() { return raceResult != null; }
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
    public String getImagePath() { return "/circuitimages/"+circuitName.replaceAll(" ","")+".png"; }
    public LocalDateTime getUpcomingDate() { return localDateTime.minusDays(2).withHour(7); }
}
