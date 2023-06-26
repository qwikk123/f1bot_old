package qwikk.f1bot.f1data;

import qwikk.f1bot.Main;
import qwikk.f1bot.ergastparser.ErgastParser;
import qwikk.f1bot.scheduling.MessageScheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class F1Data {
    private static F1Data f1data;
    private ArrayList<Race> raceList;
    private HashMap<String, Driver> driverMap;
    private ArrayList<Constructor> constructorStandings;
    private Race nextRace;
    private final MessageScheduler messageScheduler;
    private final ErgastParser ergastParser;
    private static final String scheduledTextChannel = "f1";

    private F1Data() {
        ergastParser = new ErgastParser();
        messageScheduler = new MessageScheduler(Main.bot.getTextChannelsByName(scheduledTextChannel,true));
        setData();
    }
    public static F1Data getF1Data() {
        if (f1data == null) { f1data = new F1Data(); }
        return f1data;
    }

    public void setData() {
        ArrayList<Race> newRaceList = ergastParser.getF1RaceData(raceList == null);
        if (newRaceList != null) {
            raceList = newRaceList;
            setNextRace();
        }

        HashMap<String, Driver> newDriverMap = ergastParser.getF1DriverStandingsData(driverMap == null);
        if (newDriverMap != null) { driverMap = newDriverMap; }

        ArrayList<Constructor> newConstructorStandings = ergastParser.getF1ConstructorStandingsData(constructorStandings == null);
        if (newConstructorStandings != null) { constructorStandings = newConstructorStandings; }

        ArrayList<RaceResult> raceResults = ergastParser.getRaceResults(raceList.get(0).getRaceResult() == null);
        if (raceResults != null) {
            for (int i = 0; i < raceResults.size(); i++) {
                raceList.get(i).setRaceResult(raceResults.get(i));
            }
        }

//      Any more api requests will require a delay. Max 4 polls per second/ 200 per hour
    }

    public void setNextRace() {
        for (Race r : raceList) {
            if (r.getLocalDateTime().isAfter(LocalDateTime.now())) {
                nextRace = r;
                refreshScheduler();
                return;
            }
        }
    }

    public void refreshScheduler() {
        if (nextRace.getLocalDateTime().minusDays(2).isAfter(LocalDateTime.now())) {
            messageScheduler.setChannelList(Main.bot.getTextChannelsByName(scheduledTextChannel,true));
            messageScheduler.cancel();
            messageScheduler.schedule(nextRace);
        }
    }

    public ArrayList<Race> getRaceList() { return raceList; }
    public Race getRace(int i) { return raceList.get(i); }
    public Race getNextRace() { return nextRace; }
    public HashMap<String, Driver> getDriverMap() { return driverMap; }
    public ArrayList<Constructor> getConstructorStandings() { return constructorStandings; }
    public Driver getDriver(String code) { return driverMap.get(code); }
}
