package qwikk.f1bot.datasource;

import qwikk.f1bot.model.Constructor;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.model.Race;
import qwikk.f1bot.model.RaceResult;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing the source of F1 data.
 */
public class F1DataSource {
    private ArrayList<Race> raceList;
    private HashMap<String, Driver> driverMap;
    private ArrayList<Constructor> constructorStandings;
    private final ErgastParser ergastParser;

    /**
     * Creates an instance of F1DataSource and initializes its ErgastParser.
     */
    public F1DataSource() {
        ergastParser = new ErgastParser();
    }

    /**
     * Method that updates all F1 data tied to this datasource.
     * It updates the raceList, driverMap, constructorStandings and the results tied to races that have any.
     * If the ErgastParser returns null it will view the data as up to date.
     * @return true if any of the data was updated or false if nothing changed.
     */
    public boolean setData() {
        boolean updated = false;
        ArrayList<Race> newRaceList = ergastParser.getF1RaceData(raceList == null);
        if (newRaceList != null) {
            raceList = newRaceList;
            updated = true;
        }

        HashMap<String, Driver> newDriverMap = ergastParser.getF1DriverStandingsData(driverMap == null);
        if (newDriverMap != null) { driverMap = newDriverMap; updated = true;}

        ArrayList<Constructor> newConstructorStandings = ergastParser.getF1ConstructorStandingsData(constructorStandings == null);
        if (newConstructorStandings != null) { constructorStandings = newConstructorStandings; updated = true;}

        ArrayList<RaceResult> raceResults = ergastParser.getRaceResults(raceList.get(0).getRaceResult() == null);
        if (raceResults != null) {
            for (int i = 0; i < raceResults.size(); i++) {
                raceList.get(i).setRaceResult(raceResults.get(i));
            }
            updated = true;
        }
        //      Any more api requests will require a delay. Max 4 polls per second/ 200 per hour
        return updated;
    }

    public ArrayList<Race> retrieveRaceList() { return raceList; }
    public HashMap<String, Driver> retrieveDriverMap() { return driverMap; }
    public ArrayList<Constructor> retrieveConstructorStandings() { return constructorStandings; }
}
