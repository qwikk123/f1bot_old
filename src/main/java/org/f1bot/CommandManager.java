package org.f1bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    F1Data f1Data;

    public CommandManager(F1Data f1Data) {
        super();
        this.f1Data = f1Data;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            long ping = OffsetDateTime.now().until(event.getTimeCreated(), ChronoUnit.MILLIS);
            event.reply("pong after: "+ping+" ms :)").queue();
        }
        else if (event.getName().equals("getrace")) {
            int index = event.getOption("racenumber").getAsInt()-1;
            if (!f1Data.hasRace(index)) {event.reply("invalid racenumber: "+index+1).queue();return;}
            Race race = f1Data.getRace(index);
            event.replyEmbeds(createRaceEmbed(race).build()).queue();
        }
        else if (event.getName().equals("nextrace")) {
            Race race = f1Data.getNextRace();
            event.replyEmbeds(createRaceEmbed(race).build()).queue();
        }
        else if (event.getName().equals("driverstandings")) {
            event.replyEmbeds(createStandingsEmbed(f1Data.getDriverStandings()).build()).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping", "check if don is sleeping :)"));
        commandData.add(Commands.slash("getrace", "nth race").addOption(OptionType.INTEGER, "racenumber", "Race to get info from", true, true));
        commandData.add(Commands.slash("nextrace", "Get the upcoming Grand Prix"));
        commandData.add(Commands.slash("driverstandings", "Get info on driver championship standings"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    public EmbedBuilder createRaceEmbed(Race r) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(r.name);
        eb.setColor(Color.RED);
        eb.addField("Circuit: ", r.circuitName,false);
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        return eb;
    }
    public EmbedBuilder createStandingsEmbed(ArrayList<Driver> driverStandings) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Driver Standings");
        eb.setColor(Color.RED);
        for (Driver d : driverStandings) {
            eb.addField("#"+d.pos+" "+d.name, d.constructorName+"\nPoints: "+d.points, true);
        }
        if (driverStandings.size()%3 == 2) eb.addField("","",true); //shift last row as if 3 elements if there are only 2
        return eb;
    }
}
