package qwikk.f1bot.model;

import java.util.ArrayList;

public class RaceResult {
    private final ArrayList<ResultDriver> raceResult;

    public RaceResult(ArrayList<ResultDriver> raceResultIds) {
        this.raceResult = raceResultIds;
    }

    public ArrayList<ResultDriver> getRaceResultList() { return raceResult; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ResultDriver d : raceResult) {
            sb.append(d.driverId());
            sb.append("\n");
        }
        return sb.toString();
    }
}
