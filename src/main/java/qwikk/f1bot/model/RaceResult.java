package qwikk.f1bot.model;

import java.util.ArrayList;

/**
 * Class that contains a list of ResultDrivers(Driver ids and their race results) for a race.
 */
public class RaceResult {
    private final ArrayList<DriverResult> driverResultList;

    /**
     * Creates a instance of RaceResult
     * @param driverResultList a list containing ResultDrivers(Driver ids and their race result)
     */
    public RaceResult(ArrayList<DriverResult> driverResultList) {
        this.driverResultList = driverResultList;
    }

    public ArrayList<DriverResult> getRaceResultList() { return driverResultList; }
}
