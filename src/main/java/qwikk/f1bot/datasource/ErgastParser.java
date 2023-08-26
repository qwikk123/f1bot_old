package qwikk.f1bot.datasource;

import org.json.JSONArray;
import org.json.JSONObject;
import qwikk.f1bot.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Class for parsing data from the Ergast API for the f1bot's model classes and datasource.
 */
public class ErgastParser {

    private final ErgastDataRetriever ergastDataRetriever;
    private final HashMap<String, String> countryCodeMap;

    /**
     * Creates an instance of the ErgastParser and initializes an ErgastDataRetriever.
     */
    public ErgastParser() {
        ergastDataRetriever = new ErgastDataRetriever();
        countryCodeMap = new HashMap<>();
        for (Locale iso : Locale.getAvailableLocales()) {
            countryCodeMap.put(iso.getDisplayCountry(), iso.getCountry());
        }
        countryCodeMap.put("UAE", "ae");
        countryCodeMap.put("USA", "us");
        countryCodeMap.put("UK", "gb");
    }

    /**
     * Parses information from an ErgastDataRetriever.
     * The data is parsed as a JSON object.
     * The method will not update anything unless validUpdate() returns true.
     * @param forceUpdate forces the update of data (this is mostly used for initial data retrieval on bot startup)
     * @return A list containing a Race instance for each race in the F1 season.
     */
    public ArrayList<Race> getF1RaceData(boolean forceUpdate) {
        String URL = "https://ergast.com/api/f1/current.json";
        boolean validUpdate = ergastDataRetriever.validUpdate(URL);
        if (!forceUpdate && !validUpdate) { return null; }

        JSONObject json = ergastDataRetriever.getJson(URL, validUpdate);
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("RaceTable")
                .getJSONArray("Races");

        ArrayList<Race> raceList = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jRace = jArray.getJSONObject(i);
            Instant raceInstant = getInstant(jRace.getString("date"), jRace.getString("time"));
            int round = jRace.getInt("round");

            JSONObject jQualifying = jRace.getJSONObject("Qualifying");

            Instant qualiInstant = getInstant(jQualifying.getString("date"), jQualifying.getString("time"));

            JSONObject circuit = jRace.getJSONObject("Circuit");
            String circuitName = circuit.getString("circuitName");

            String name = jRace.getString("raceName");
            String countryName = jRace.getJSONObject("Circuit").getJSONObject("Location").getString("country");
            String countryCode = countryCodeMap.get(countryName);
            Race r = new Race(name, circuitName, raceInstant, qualiInstant, round, countryCode);
            if (jRace.has("Sprint")) {
                JSONObject jSprint = jRace.getJSONObject("Sprint");
                r.setSprint(getInstant(jSprint.getString("date"), jSprint.getString("time")));
            }
            raceList.add(r);
        }
        return raceList;
    }

    /**
     * Parses information from an ErgastDataRetriever.
     * The data is parsed as a JSON object.
     * The method will not update anything unless validUpdate() returns true.
     * @param forceUpdate forces the update of data (this is mostly used for initial data retrieval on bot startup)
     * @return A List containing RaceResults for each race in the F1 season
     */
    public ArrayList<RaceResult> getRaceResults(boolean forceUpdate) {
        ArrayList<RaceResult> raceResults = new ArrayList<>();

        String URL = "https://ergast.com/api/f1/current/results.json?limit=1000";
        boolean validUpdate = ergastDataRetriever.validUpdate(URL);
        if (!forceUpdate && !validUpdate) { return null; }

        JSONObject json = ergastDataRetriever.getJson(URL, validUpdate);
        JSONArray jArray = json.getJSONObject("MRData")
                .getJSONObject("RaceTable")
                .getJSONArray("Races");

        for (int i = 0; i < jArray.length(); i++) {
            RaceResult raceResult = new RaceResult(new ArrayList<>());
            JSONObject race = jArray.getJSONObject(i);
            JSONArray resultArray = race.getJSONArray("Results");

            for (int j = 0; j < resultArray.length(); j++) {

                JSONObject jsonDriver = resultArray.getJSONObject(j);
                String driverId = jsonDriver.getJSONObject("Driver").getString("driverId");
                int laps = jsonDriver.getInt("laps");
                int gridStart = jsonDriver.getInt("grid");
                String status = jsonDriver.getString("status");
                int points = jsonDriver.getInt("points");

                ResultDriver rDriver = new ResultDriver(driverId, laps, gridStart, status, points);

                raceResult.getRaceResultList().add(rDriver);
            }
            raceResults.add(raceResult);
        }
        return raceResults;
    }

    /**
     * Parses information from an ErgastDataRetriever.
     * The data is parsed as a JSON object.
     * The method will not update anything unless validUpdate() returns true.
     * @param forceUpdate forces the update of data (this is mostly used for initial data retrieval on bot startup)
     * @return A HashMap containing all the drivers in the F1 season mapped to their driverId
     */
    public HashMap<String, Driver> getF1DriverStandingsData(boolean forceUpdate) {
        String URL = "https://ergast.com/api/f1/current/driverStandings.json";
        boolean validUpdate = ergastDataRetriever.validUpdate(URL);
        if (!forceUpdate && !validUpdate) { return null; }

        JSONObject json = ergastDataRetriever.getJson(URL, validUpdate);
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

    /**
     * Parses information from an ErgastDataRetriever.
     * The data is parsed as a JSON object.
     * The method will not update anything unless validUpdate() returns true.
     * @param forceUpdate forces the update of data (this is mostly used for initial data retrieval on bot startup)
     * @return A list containing the constructors for this F1 season.
     */
    public ArrayList<Constructor> getF1ConstructorStandingsData(boolean forceUpdate) {
        String URL = "https://ergast.com/api/f1/current/constructorStandings.json";
        boolean validUpdate = ergastDataRetriever.validUpdate(URL);
        if (!forceUpdate && !validUpdate) { return null; }

        JSONObject json = ergastDataRetriever.getJson(URL, validUpdate);
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

    /**
     * Gets a LocalDateTime object from separate string and time strings. (Strings missing the T separator)
     * @param date String representing date
     * @param time String representing time
     * @return A new LocalDateTime object.
     */
    public Instant getInstant(String date, String time) {
        return Instant.parse(date+"T"+time);
    }
}
