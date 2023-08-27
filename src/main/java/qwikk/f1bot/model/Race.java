package qwikk.f1bot.model;

import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Class that represents an F1 race.
 */
public class Race {
    private final String name;
    private final String circuitName;
    private final Instant raceInstant;
    private final Instant qualiInstant;
    private Instant sprintInstant;
    private final int round;
    private RaceResult raceResult;
    private final String countryCode;

    /**
     * Creates an instance of Race representing an F1 Race
     *
     * @param name         name of the race
     * @param circuitName  name of the circuit the race is using
     * @param raceInstant  the datetime for when the race starts
     * @param qualiInstant the datetime for when the qualifying starts
     * @param round        the race position in the calendar
     * @param countryCode  The name of the country where this race takes place
     */
    public Race(String name, String circuitName, Instant raceInstant, Instant qualiInstant, int round, String countryCode) {
        this.name = name;
        this.raceInstant = raceInstant;
        this.qualiInstant = qualiInstant;
        this.circuitName = circuitName;
        this.round = round;
        this.countryCode = countryCode;
    }


    public void setRaceResult(RaceResult raceResult) { this.raceResult = raceResult; }
    public RaceResult getRaceResult() { return raceResult; }
    public boolean hasRaceResult() { return raceResult != null; }
    public String getCountryCode() { return countryCode.toLowerCase(); }
    public String getRaceRelativeTimestamp() { return TimeFormat.RELATIVE.atInstant(raceInstant).toString(); }
    public String getRaceTimestampDateOnly() { return TimeFormat.DATE_LONG.atInstant(raceInstant).toString(); }
    public String getRaceTimestamp() { return TimeFormat.DATE_TIME_SHORT.atInstant(raceInstant).toString(); }
    public String getQualifyingTimestamp() { return TimeFormat.DATE_TIME_SHORT.atInstant(qualiInstant).toString(); }
    public String getSprintTimestamp() { return TimeFormat.DATE_TIME_SHORT.atInstant(sprintInstant).toString(); }
    public String getName() { return name; }
    public int getRound() { return round; }
    public String getCircuitName() { return circuitName; }
    public Instant getRaceInstant() { return raceInstant; }
    public void setSprint(Instant sprintInstant) { this.sprintInstant = sprintInstant; }
    public boolean hasSprint() { return sprintInstant != null; }
    public String getImagePath() { return "/circuitimages/"+circuitName.replaceAll(" ","")+".png"; }
    public Instant getUpcomingDate() { return raceInstant.minus(2, ChronoUnit.DAYS); }
}
