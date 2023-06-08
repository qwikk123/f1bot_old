package qwikk.f1bot.f1data;

import net.dv8tion.jda.api.JDA;
import qwikk.f1bot.ergastparser.ErgastAPI;
import qwikk.f1bot.scheduling.MessageScheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class F1Data {
    private ArrayList<Race> raceList;
    private HashMap<String, Driver> driverMap;
    private ArrayList<Constructor> constructorStandings;
    private Race nextRace;
    private final MessageScheduler messageScheduler;
    ErgastAPI ergastAPI;

    public F1Data(JDA bot) {
        ergastAPI = new ErgastAPI();
        messageScheduler = new MessageScheduler(bot.getTextChannelById("831261818101694524"));
        update();
    }

    public void update() {
        setF1RaceData();
        setNextRace();
        setF1DriverStandingsData();
        setF1ConstructorStandingsData();
    }

    public void setF1RaceData() {
        raceList = ergastAPI.getF1RaceData();
    }

    public void setF1DriverStandingsData() {
        driverMap = ergastAPI.getF1DriverStandingsData();
    }

    public void setF1ConstructorStandingsData() {
        constructorStandings = ergastAPI.getF1ConstructorStandingsData();
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
