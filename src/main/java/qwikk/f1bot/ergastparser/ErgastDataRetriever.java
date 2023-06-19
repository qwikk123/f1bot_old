package qwikk.f1bot.ergastparser;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ErgastDataRetriever {
    LocalDateTime lastCacheTime;

    public ErgastDataRetriever() {
        lastCacheTime = LocalDateTime.now();
    }
    public JSONObject getJson(String URL) {
        if (LocalDateTime.now().isBefore(lastCacheTime.plusHours(2))) {
            String fileName = URL
                    .replaceAll("\\/+","_")
                    .replaceAll(":", "")
                    .replaceFirst("\\.", "");
            File f = new File("cache/"+fileName);
            if (f.isFile()) {
                System.out.println("Retrieving data from cache");
                return getJsonFromFile(f);
            }
        }
        System.out.println("Retrieving data from ergast");
        return getJsonFromURL(URL);
    }

    private JSONObject getJsonFromFile(File f) {
        try {
            String json = Files.readString(Paths.get(f.getPath()));
            return new JSONObject(new JSONTokener(json));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getJsonFromURL(String URL) {
        try {
            java.net.URL url = new URI(URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String json = bufferedReader.lines().collect(Collectors.joining());

            String fileName = URL
                    .replaceAll("\\/+","_")
                    .replaceAll(":", "")
                    .replaceFirst("\\.", "");

            File f = new File("cache/"+fileName);
            System.out.println(f.getPath());
            FileWriter fileWriter = new FileWriter(f, false);
            fileWriter.write(json);
            fileWriter.close();
            lastCacheTime = LocalDateTime.now();
            System.out.println("Cache updated at: "+lastCacheTime);

            return new JSONObject(new JSONTokener(json));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
