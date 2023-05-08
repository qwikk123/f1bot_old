package qwikk.f1bot;

import net.dv8tion.jda.api.EmbedBuilder;
import qwikk.f1bot.f1data.Constructor;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.f1data.Race;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
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

    public static EmbedBuilder createDriverStandings(ArrayList<Driver> driverStandings, int page) {
        int pageSize = 10;
        int start = pageSize*page;
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Driver Standings");
        for (Driver driver : driverStandings.subList(start,start+pageSize)) {
            eb.addField("#"+driver.pos()+" "+driver.name(), driver.constructorName()+"\nPoints: "+df.format(driver.points()), true);
        }
        eb.setFooter(Integer.toString(page));
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
        if(r.hasSprint()) { eb.addField("Sprint: ", r.getSprintDateAsString(),true); }
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        eb.addField("Circuit: ", r.getCircuitName(),false);
        eb.setImage("attachment://"+"circuitImage.png");
        return eb;
    }
}
