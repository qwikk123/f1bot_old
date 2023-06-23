package qwikk.f1bot.f1data;

import java.util.ArrayList;

public class RaceResult {
    private final ArrayList<String> raceResult;

    public RaceResult(ArrayList<String> raceResultIds) {
        this.raceResult = raceResultIds;
    }

    public ArrayList<String> getRaceResultList() { return raceResult; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : raceResult) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
