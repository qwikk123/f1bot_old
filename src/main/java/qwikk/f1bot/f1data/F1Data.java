package qwikk.f1bot.f1data;

import net.dv8tion.jda.api.JDA;
import qwikk.f1bot.ergastparser.ErgastParser;
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
    private final ErgastParser ergastAPI;
    private final JDA bot;
    private LocalDateTime lastUpdate;
    private static final String scheduledTextChannel = "f1";

    public F1Data(JDA bot) {
        this.bot = bot;
        lastUpdate = LocalDateTime.now();
        ergastAPI = new ErgastParser();
        messageScheduler = new MessageScheduler(bot.getTextChannelsByName(scheduledTextChannel,true));
        setData();
    }

    public void update() {
        if (!lastUpdate.plusHours(2).isBefore(LocalDateTime.now())) { return; }
        lastUpdate = LocalDateTime.now();
        setData();
    }

    public void setData() {
        raceList = ergastAPI.getF1RaceData();
        setNextRace();
        driverMap = ergastAPI.getF1DriverStandingsData();
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
            messageScheduler.setChannelList(bot.getTextChannelsByName(scheduledTextChannel,true));
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
