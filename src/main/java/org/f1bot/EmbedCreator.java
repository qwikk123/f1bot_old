package org.f1bot;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;

public class EmbedCreator {
    public static EmbedBuilder createRace(Race r) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://i.imgur.com/7wyu3ng.png");
        eb.setTitle(r.name);
        eb.setColor(Color.RED);
        eb.addField("Circuit: ", r.circuitName,false);
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        if(r.hasSprint()) { eb.addField("Sprint: ", r.getSprintDateAsString(),true); }
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        return eb;
    }
    public static EmbedBuilder createDriverStandings(ArrayList<Driver> driverStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://i.imgur.com/7wyu3ng.png");
        eb.setTitle("Driver Standings");
        eb.setColor(Color.RED);
        for (Driver d : driverStandings) {
            eb.addField("#"+d.pos+" "+d.name, d.constructorName+"\nPoints: "+d.points, true);
        }
        return eb;
    }
    public static EmbedBuilder createConstructorStandings(ArrayList<Constructor> constructorStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://i.imgur.com/7wyu3ng.png");
        eb.setTitle("Constructor Standings");
        eb.setColor(Color.RED);
        for (Constructor d : constructorStandings) {
            eb.addField("#"+d.pos+" "+d.name, "\nPoints: "+d.points, true);
        }
        return eb;
    }

    public static EmbedBuilder createUpcoming(Race r) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail("https://i.imgur.com/7wyu3ng.png");
        eb.setTitle("This weekend: "+r.name);
        eb.setColor(Color.RED);
        eb.addField("Circuit: ", r.circuitName,false);
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        if(r.hasSprint()) { eb.addField("Sprint: ", r.getSprintDateAsString(),true); }
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        return eb;
    }
}
