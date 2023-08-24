package qwikk.f1bot.model;

import java.util.ArrayList;

/**
 * Class that contains a list of ResultDrivers(Driver ids and their race results) for a race.
 */
public class RaceResult {
    private final ArrayList<ResultDriver> resultDriverList;

    /**
     * Creates a instance of RaceResult
     * @param resultDriverList a list containing ResultDrivers(Driver ids and their race result)
     */
    public RaceResult(ArrayList<ResultDriver> resultDriverList) {
        this.resultDriverList = resultDriverList;
    }

    public ArrayList<ResultDriver> getRaceResultList() { return resultDriverList; }
}
