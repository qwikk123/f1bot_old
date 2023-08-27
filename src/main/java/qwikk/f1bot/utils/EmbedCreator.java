package qwikk.f1bot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import qwikk.f1bot.model.DriverResult;
import qwikk.f1bot.model.Constructor;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.model.Race;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

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

        eb.setTitle("#"+driver.permanentNumber()+" "+getCountryCodeEmoji(driver.isoCode())+driver.name());
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
    public static EmbedBuilder createDriverStandings(HashMap<String, Driver> driverMap, int page, int pageSize) {
        List<Driver> driverStandings = driverMap.values().stream()
                .sorted(Comparator.comparingDouble(Driver::pos))
                .toList();

        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);

        eb.setTitle("Driver Standings");
        eb.addField("Driver:","",true);
        eb.addField("Team:","",true);
        eb.addField("Points:","",true);

        int start = pageSize*page;
        for (Driver driver : driverStandings.subList(start,Math.min(start+pageSize, driverStandings.size()))) {
            eb.addField("#"+driver.pos()+" "+getCountryCodeEmoji(driver.isoCode())+driver.name(),"",true);
            eb.addField(driver.constructorName(),"",true);
            eb.addField(df.format(driver.points()), "", true);
        }

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
            eb.addField("#"+constructor.pos()+" "+constructor.name(),
                    "\nPoints: "+df.format(constructor.points()),
                    true);
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

        eb.setTitle(extraTitle+"#"+r.getRound()+" "+getCountryCodeEmoji(r.getCountryCode())+" "+r.getName());
        eb.addField("Race: ", r.getRaceTimestamp()+"\n"+r.getRaceRelativeTimestamp(),true);

        if(r.hasSprint()) eb.addField("Sprint: ", r.getSprintTimestamp(),true);

        eb.addField("Qualifying: ", r.getQualifyingTimestamp(),true);
        eb.addField("Circuit: ", r.getCircuitName(),false);
        eb.setImage("attachment://circuitImage.png");

        return eb;
    }

    /**
     * Creates an EmbedBuilder for a race result page.
     * It creates a table format inside a discord codeblock.
     * @param r the race to create a result embed for.
     * @param driverMap a list of drivers in the current F1 season.
     * @param page the page to create the embed for.
     * @return an EmbedBuilder containing the result page.
     */
    public static EmbedBuilder createRaceResult(Race r,HashMap<String, Driver> driverMap, int page, int pageSize) {
        List<DriverResult> raceResultList = r.getRaceResult().getRaceResultList();

        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("#"+r.getRound()+" "+getCountryCodeEmoji(r.getCountryCode())+r.getName());

        int start = pageSize*page;

        String format = "%-4s  %-16s  %-7s  %s";
        String codeBlockText = "```"+String.format(format,"Pos:","Driver:","Points:","Status:")+"\n";
        for (DriverResult driverResult : raceResultList.subList(start, Math.min(start+pageSize, raceResultList.size()))) {
            Driver d = driverMap.get(driverResult.driverId());
            codeBlockText += String.format(format,
                    "#"+driverResult.pos(),          //Pos
                    d.name(),                        //Driver
                    df.format(driverResult.points()),//Points
                    driverResult.status())           //Status
                    +"\n";
        }
        codeBlockText += "```";
        eb.addField("Result",codeBlockText,true);

        int maxPage = (int) Math.ceil((double)raceResultList.size()/pageSize);
        eb.setFooter((page+1)+"/"+maxPage);

        return eb;
    }

    /**
     * Creates an EmbedBuilder for the season calendar.
     * @param raceList list of races in the current F1 season.
     * @return an EmbedBuilder containing the season calendar
     */
    public static EmbedBuilder createCalendar(List<Race> raceList) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Calendar");

        for (Race r : raceList) {
            String countryCodeEmoji = ":flag_"+r.getCountryCode()+":";
            eb.addField("#"+r.getRound()+" "+countryCodeEmoji+" "+r.getName(),
                    r.getRaceTimestampDateOnly(),
                    true);
        }

        return eb;
    }

    /**
     * Function to convert a country iso code into a discord flag emoji.
     * :flag_ISOCODE:
     * @param isoCode the iso country code to make an emoji for.
     * @return a string containing a discord flag emoji.
     */
     private static String getCountryCodeEmoji(String isoCode) {
        return ":flag_"+isoCode+":";
     }
}
