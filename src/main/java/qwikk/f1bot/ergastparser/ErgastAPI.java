package qwikk.f1bot.ergastparser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import qwikk.f1bot.f1data.Constructor;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.f1data.Race;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

public class ErgastAPI {
    LocalDateTime lastCacheTime;
    public ErgastAPI () {
        lastCacheTime = LocalDateTime.now();
    }

    public JSONObject getJson(String URL) {
        //TODO caching to json file
        return getJsonFromURL(URL);
    }

    public JSONObject getJsonFromURL(String URL) {
        try {
            URL url = new URI(URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            return new JSONObject(new JSONTokener(url.openStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Race> getF1RaceData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("RaceTable")
                .getJSONArray("Races");

        ArrayList<Race> raceList = new ArrayList<>();

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
        return raceList;
    }

    public HashMap<String, Driver> getF1DriverStandingsData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current/driverStandings.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0).getJSONArray("DriverStandings");

        HashMap<String, Driver> driverMap = new HashMap<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jDriver = jArray.getJSONObject(i);
            JSONObject jDriverInfo = jDriver.getJSONObject("Driver");
            JSONObject jDriverConstructor = jDriver.getJSONArray("Constructors").getJSONObject(0);

            int pos = jDriver.getInt("position");
            String name = jDriverInfo.getString("givenName")+" "+jDriverInfo.getString("familyName");
            double points = jDriver.getDouble("points");
            int wins = jDriver.getInt("wins");
            String constructorName = jDriverConstructor.getString("name");
            String code = jDriverInfo.getString("code");
            String nationality = jDriverInfo.getString("nationality");
            String driverId = jDriverInfo.getString("driverId");
            int permanentNumber = jDriverInfo.getInt("permanentNumber");

            driverMap.put(driverId, new Driver(pos,name,constructorName,points,wins, code, nationality, driverId, permanentNumber));
        }

        return driverMap;
    }

    public ArrayList<Constructor> getF1ConstructorStandingsData() {
        JSONObject json = getJson("https://ergast.com/api/f1/current/constructorStandings.json");
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("StandingsTable")
                .getJSONArray("StandingsLists")
                .getJSONObject(0).getJSONArray("ConstructorStandings");

        ArrayList<Constructor> constructorStandings = new ArrayList<>();

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

        return constructorStandings;
    }

    public LocalDateTime getLocalDateTime (String date, String time) {
        return Instant.parse(date+"T"+time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
