package org.f1bot;

import net.dv8tion.jda.api.JDA;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class F1Data {
    private ArrayList<Race> raceList;
    private ArrayList<Driver> driverStandings;
    private ArrayList<Constructor> constructorStandings;
    private Race nextRace;
    private final MessageScheduler messageScheduler;

    public F1Data(JDA bot) {
        messageScheduler = new MessageScheduler(bot, this);
        update();
        messageScheduler.schedule();
    }

    public void update() {
        setF1RaceData();
        setNextRace();
        setF1DriverStandingsData();
        setF1ConstructorStandingsData();
    }

    public void setF1RaceData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("RaceTable")
                .getJSONArray("Races");

        raceList = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jRace = jArray.getJSONObject(i);
            LocalDateTime ldt = getLocalDateTime(jRace.getString("date"), jRace.getString("time"));
            int round = jRace.getInt("round");

            JSONObject jQualifying = jRace.getJSONObject("Qualifying");
            LocalDateTime ldtq = getLocalDateTime(jQualifying.getString("date"), jQualifying.getString("time"));

            JSONObject circuit = jRace.getJSONObject("Circuit");
            String circuitName = circuit.getString("circuitName");

            String name = jRace.getString("raceName");
            Race r = new Race(name, circuitName, ldt, ldtq, round);
            if (jRace.has("Sprint")) {
                JSONObject jSprint = jRace.getJSONObject("Sprint");
                r.setSprint(getLocalDateTime(jSprint.getString("date"), jSprint.getString("time")));
            }
            raceList.add(r);
        }
    }

    public void setF1DriverStandingsData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current/driverStandings.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0).getJSONArray("DriverStandings");

        driverStandings = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jDriver = jArray.getJSONObject(i);
            JSONObject jDriverInfo = jDriver.getJSONObject("Driver");
            JSONObject jDriverConstructor = jDriver.getJSONArray("Constructors").getJSONObject(0);

            int pos = jDriver.getInt("position");
            String name = jDriverInfo.getString("givenName")+" "+jDriverInfo.getString("familyName");
            double points = jDriver.getDouble("points");
            int wins = jDriver.getInt("wins");
            String constructorName = jDriverConstructor.getString("name");

            driverStandings.add(new Driver(pos,name,constructorName,points,wins));
        }

    }

    public void setF1ConstructorStandingsData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current/constructorStandings.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0).getJSONArray("ConstructorStandings");

        constructorStandings = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jConstructor = jArray.getJSONObject(i);
            JSONObject jConstructorInfo = jConstructor.getJSONObject("Constructor");

            int pos = jConstructor.getInt("position");
            String name = jConstructorInfo.getString("name");
            String nationality = jConstructorInfo.getString("nationality");
            double points = jConstructor.getDouble("points");
            int wins = jConstructor.getInt("wins");

            constructorStandings.add(new Constructor(pos,name,nationality,points,wins));
        }
    }

    public void setNextRace() {
        for (Race r : raceList) {
            if (r.getLocalDateTime().isAfter(LocalDateTime.now())) {
                nextRace = r;
                if (messageScheduler.hasUpcomingRaceFuture() && r.getLocalDateTime().minusDays(2).isAfter(LocalDateTime.now())) {
                    messageScheduler.cancel();
                    messageScheduler.schedule();
                }
                return;
            }
        }
    }

    public LocalDateTime getLocalDateTime (String date, String time) {
        return Instant.parse(date+"T"+time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public boolean hasRace(int i) { return i>=0&&i<raceList.size(); }
    public ArrayList<Race> getRaceList() { return raceList; }
    public Race getRace(int i) { return raceList.get(i); }
    public Race getNextRace() { return nextRace; }
    public ArrayList<Driver> getDriverStandings() { return driverStandings; }
    public ArrayList<Constructor> getConstructorStandings() { return constructorStandings; }

    public JSONObject getJson(String URL) {
        try {
            URL url = new URI(URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();
            if (responsecode != 200) throw new RuntimeException("HttpResponseCode: " + responsecode);
            else {
                return new JSONObject(new JSONTokener(url.openStream()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
