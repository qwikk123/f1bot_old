package qwikk.f1bot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import qwikk.f1bot.model.ResultDriver;
import qwikk.f1bot.model.Constructor;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.model.Race;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Class containing utility functions for creating embed messages.
 */
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

    /**
     * Sets the general theme of an EmbedBuilder
     * @param eb EmbedBuilder to set the theme for.
     */
    public static void setTheme(EmbedBuilder eb) {
        eb.setThumbnail(thumbnailURL);
        eb.setColor(color);
    }

    /**
     * Creates an EmbedBuilder for the driver profile message.
     * @param driver the driver to create a profile for
     * @return an EmbedBuilder with the driver profile
     */
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

    /**
     * Creates an EmbedBuilder for the driver standings message.
     * @param driverMap Map of the drivers for this F1 season.
     * @param page The page to create standings for.
     * @return an EmbedBuilder with the driver standings.
     */
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
        for (Driver driver : driverStandings.subList(start,Math.min(start+pageSize, driverStandings.size()))) {
            String line = String.format("%-25s%s\nTeam: %s","#"+driver.pos()+" "+driver.name(),df.format(driver.points()), driver.constructorName());
            fieldText+=line+"\n\n";
        }
        fieldText+="```";
        eb.addField("",fieldText,false);
        int maxPage = (int) Math.ceil((double)driverStandings.size()/pageSize);
        eb.setFooter((page+1)+"/"+maxPage);
        return eb;
    }

    /**
     * Creates an EmbedBuilder for the constructor standings message.
     * @param constructorStandings A list with the constructors from this F1 season in sorted order.
     * @return an EmbedBuilder containing the constructor standings.
     */
    public static EmbedBuilder createConstructorStandings(ArrayList<Constructor> constructorStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Constructor Standings");
        for (Constructor constructor : constructorStandings) {
            eb.addField("#"+constructor.pos()+" "+constructor.name(), "\nPoints: "+df.format(constructor.points()), true);
        }
        return eb;
    }

    /**
     * Creates an EmbedBuilder for a race message.
     * @param r the race to create a message for.
     * @param extraTitle extra prefix for the embeds title.
     * @return an EmbedBuilder containing the race.
     */
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

    /**
     * Creates an EmbedBuilder for a race result page.
     * @param r the race to create a result embed for.
     * @param driverMap a list of drivers in the current F1 season.
     * @param page the page to create the embed for.
     * @return an EmbedBuilder containing the result page.
     */
    public static EmbedBuilder createRaceResult(Race r,HashMap<String, Driver> driverMap, int page) {
        String format = "%-4s  %-16s  %-7s  %s";
        int pageSize = 10;
        int start = pageSize*page;
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("#"+r.getRound()+" "+r.getName());
        String fieldText = "```"+String.format(format,"Pos:","Driver:","Points:","Status:")+"\n";
        List<ResultDriver> raceResultList = r.getRaceResult().getRaceResultList();
        int pos = start;
        for (ResultDriver rDriver : raceResultList.subList(start, start+pageSize)) {
            Driver d = driverMap.get(rDriver.driverId());
            fieldText += String.format(format,
                    "#"+((pos++)+1),
                    d.name(),
                    df.format(rDriver.points()),
                    rDriver.status())
                    +"\n";
        }
        fieldText += "```";
        eb.addField("Result",fieldText,true);
        eb.setFooter((page+1)+"/"+raceResultList.size()/pageSize);
        return eb;
    }
}
