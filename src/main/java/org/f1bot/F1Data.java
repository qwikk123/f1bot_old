package org.f1bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class F1Data {
    ArrayList<Race> raceList;
    Race nextRace;
    public void setF1Data() {
        try {
            URL url = new URI("https://ergast.com/api/f1/current.json").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }

            else {
                JSONObject json = new JSONObject(new JSONTokener(url.openStream()));
                JSONArray jArray = json.getJSONObject("MRData").getJSONObject("RaceTable").getJSONArray("Races");
                raceList = new ArrayList<>();

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jRace = jArray.getJSONObject(i);
                    LocalDateTime ldt = getLocalDateTime(jRace.getString("date"), jRace.getString("time"));

                    JSONObject jQualifying = jRace.getJSONObject("Qualifying");
                    LocalDateTime ldtq = getLocalDateTime(jQualifying.getString("date"), jQualifying.getString("time"));

                    String name = jRace.getString("raceName");

                    raceList.add(new Race(name, ldt, ldtq));
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setNextRace() {
        for (Race r : raceList) {
            if (r.localDateTime.isAfter(LocalDateTime.now())) {
                nextRace = r;
                return;
            }
        }
    }

    public boolean hasRace(int i) {
        return i>0&&i<raceList.size();
    }

    public LocalDateTime getLocalDateTime (String date, String time) {
        OffsetDateTime odt = Instant.parse(date+"T"+time).atOffset(ZoneOffset.ofHours(2)); //UTC+2
        return odt.toLocalDateTime();
    }

    public Race getRace(int i) {
        return raceList.get(i);
    }

    public Race getNextRace() {
        return nextRace;
    }
}
