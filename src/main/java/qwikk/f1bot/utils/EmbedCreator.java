package qwikk.f1bot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import qwikk.f1bot.f1data.Constructor;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.f1data.Race;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EmbedCreator {
    private static final Color color = Color.RED;
    private static final String thumbnailURL = "https://i.imgur.com/7wyu3ng.png";
    private static final DecimalFormat df = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));

    public static EmbedBuilder createRace(Race r) {
        return createRace(r,"");
    }
    public static EmbedBuilder createUpcoming(Race r) {
        return createRace(r,"This weekend: ");
    }

    public static void setTheme(EmbedBuilder eb) {
        eb.setThumbnail(thumbnailURL);
        eb.setColor(color);
    }

    public static EmbedBuilder createDriverProfile(Driver driver) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle(driver.permanentNumber()+" "+driver.name());
        eb.addField("Team:", driver.constructorName(), false);
        eb.addField("Position", "#"+driver.pos(), true);
        eb.addField("Wins", String.valueOf(driver.wins()), true);
        eb.addField("Points", df.format(driver.points()), true);
        eb.setImage("attachment://driverImage.png");
        return eb;
    }

    public static EmbedBuilder createDriverStandings(HashMap<String, Driver> driverMap, int page) {
        List<Driver> driverStandings = driverMap.values().stream()
                .sorted(Comparator.comparingDouble(Driver::pos))
                .toList();
        int pageSize = 5;
        int start = pageSize*page;
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Driver Standings");
        String fieldText = String.format("```%-25s%s\n\n", "Driver:","Points:");
        for (Driver driver : driverStandings.subList(start,start+pageSize)) {
            String line = String.format("%-25s%s\nTeam: %s","#"+driver.pos()+" "+driver.name(),df.format(driver.points()), driver.constructorName());
            fieldText+=line+"\n\n";
        }
        fieldText+="```";
        eb.addField("",fieldText,false);
        eb.setFooter((page+1)+"/"+driverStandings.size()/pageSize);
        return eb;
    }

    public static EmbedBuilder createConstructorStandings(ArrayList<Constructor> constructorStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Constructor Standings");
        for (Constructor constructor : constructorStandings) {
            eb.addField("#"+constructor.pos()+" "+constructor.name(), "\nPoints: "+df.format(constructor.points()), true);
        }
        return eb;
    }

    private static EmbedBuilder createRace(Race r, String extraTitle) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle(extraTitle+"#"+r.getRound()+" "+r.getName());
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        if(r.hasSprint()) eb.addField("Sprint: ", r.getSprintDateAsString(),true);
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        eb.addField("Circuit: ", r.getCircuitName(),false);
        eb.setImage("attachment://circuitImage.png");
        return eb;
    }

    public static EmbedBuilder createRaceResult(Race r) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("#"+r.getRound()+" "+r.getName());
        String fieldText = "```";
        List<String> raceResultList = r.getRaceResult().getRaceResultList();
        for (int i = 0; i < raceResultList.size(); i+=2) {
            fieldText += String.format("%-20s%s","#"+(i+1)+" "+raceResultList.get(i),"#"+(i+2)+" "+raceResultList.get(i+1))+"\n";
        }
        fieldText += "```";
        eb.addField("Result",fieldText,true);
        return eb;
    }
}
