package org.f1bot;

import net.dv8tion.jda.api.EmbedBuilder;
import org.f1bot.f1data.Constructor;
import org.f1bot.f1data.Driver;
import org.f1bot.f1data.Race;

import java.awt.Color;
import java.util.ArrayList;

public class EmbedCreator {
    private static final Color color = Color.RED;
    private static final String thumbnailURL = "https://i.imgur.com/7wyu3ng.png";

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

    public static EmbedBuilder createDriverStandings(ArrayList<Driver> driverStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Driver Standings");
        for (Driver d : driverStandings) {
            eb.addField("#"+d.pos()+" "+d.name(), d.constructorName()+"\nPoints: "+d.points(), true);
        }
        return eb;
    }

    public static EmbedBuilder createConstructorStandings(ArrayList<Constructor> constructorStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle("Constructor Standings");
        for (Constructor d : constructorStandings) {
            eb.addField("#"+d.pos()+" "+d.name(), "\nPoints: "+d.points(), true);
        }
        return eb;
    }

    public static EmbedBuilder createRace(Race r, String extraTitle) {
        EmbedBuilder eb = new EmbedBuilder();
        setTheme(eb);
        eb.setTitle(extraTitle+"#"+r.getRound()+" "+r.getName());
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        if(r.hasSprint()) { eb.addField("Sprint: ", r.getSprintDateAsString(),true); }
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        eb.addField("Circuit: ", r.getCircuitName(),false);
        eb.setImage("attachment://"+r.getImagePath());
        return eb;
    }
}
