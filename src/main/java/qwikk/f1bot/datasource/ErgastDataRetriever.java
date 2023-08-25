package qwikk.f1bot.datasource;

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
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Class for retrieving data from the Ergast API
 */
public class ErgastDataRetriever {

    /**
     * Method to get json information from the ergast api or the cache
     * @param URL url pointing to the Ergast API endpoint.
     * @param validUpdate Whether to use cache or retrieve new data.
     * @return a JSONObject containing Ergast API data.
     */
    public JSONObject getJson(String URL, boolean validUpdate) {
        String fileName = getFileNameOfURL(URL);
        File f = new File("cache/"+fileName);

        if (f.isFile() && !validUpdate) {
            System.out.println("Retrieving data from cache");
            return getJsonFromFile(f);
        }

        System.out.println("Retrieving data from ergast");
        return getJsonFromURL(URL);
    }

    /**
     * Method to get a JSONObject from the cache
     * @param f Which file to retrieve data from.
     * @return A JSONObject containing Ergast API data.
     */
    private JSONObject getJsonFromFile(File f) {
        try {
            String json = Files.readString(Paths.get(f.getPath()));
            return new JSONObject(new JSONTokener(json));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to get a JSONObject from an Ergast endpoint.
     * @param URL Which Ergast endpoint to retrieve data from.
     * @return A JSONObject containing Ergast API data.
     */
    private JSONObject getJsonFromURL(String URL) {
        String fileName = getFileNameOfURL(URL);
        try {
            java.net.URL url = new URI(URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String json = bufferedReader.lines().collect(Collectors.joining());

            File f = new File("cache/"+fileName);
            Files.createDirectories(Paths.get(f.getParent()));
            FileWriter fileWriter = new FileWriter(f, false);
            fileWriter.write(json);
            fileWriter.close();
            System.out.println("UPDATING: "+f.getPath());
            System.out.println("UPDATED AT: "+LocalDateTime.now());
            System.out.println();

            return new JSONObject(new JSONTokener(json));

        } catch (IOException e){
            System.out.println("FILE IO ERROR OR ERGAST CONNECTION FAILED");
            System.out.println("RETRIEVING FROM CACHE\n");
            File f = new File("cache/"+fileName);
            return getJsonFromFile(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method converting a url endpoint to a filename.
     * @param URL url of Ergast API endpoint.
     * @return String containing a filename.
     */
    private String getFileNameOfURL(String URL) {
        return URL.replaceAll("/+","_")
                .replaceAll(":", "")
                .replaceAll("\\?","")
                .replaceFirst("\\.", "");
    }

    /**
     * Method to check whether an update from the api is needed or not.
     * The update interval is set to maximum update once every 24 hours.
     * @param URL url of Ergast Endpoint.
     * @return true if the cache is out of date.
     */
    public boolean validUpdate(String URL) {
        String fileName = getFileNameOfURL(URL);
        File f = new File("cache/"+fileName);
        try {
            if (f.isFile()) {
                FileTime ft = Files.getLastModifiedTime(Paths.get(f.getPath()));
                LocalDateTime modifiedTime = LocalDateTime.ofInstant(ft.toInstant(), ZoneId.systemDefault());
                return LocalDateTime.now().isAfter(modifiedTime.plusDays(1));
            }
            else {
                return true;
            }
        }
        catch (IOException e) {
            //use ergast for info and try to create/recreate the file;
            return true;
        }
    }
}
