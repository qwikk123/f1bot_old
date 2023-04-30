package org.f1bot;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    F1Data f1Data;
    LocalDateTime lastUpdate;

    public CommandManager(F1Data f1Data) {
        super();
        this.f1Data = f1Data;
        lastUpdate = LocalDateTime.now();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (lastUpdate.plusHours(2).isBefore(LocalDateTime.now())) {
            f1Data.update();
            System.out.println("UPDATING DATA");
            lastUpdate = LocalDateTime.now();
        }
        if (event.getName().equals("ping")) {
            long ping = event.getTimeCreated().until(OffsetDateTime.now(), ChronoUnit.MILLIS);
            event.reply("pong after: "+ping+" ms :)").queue();
        }
        else if (event.getName().equals("getrace")) {
            int index = event.getOption("racenumber").getAsInt()-1;
            if (!f1Data.hasRace(index)) { event.reply("invalid racenumber: "+(index+1)).queue(); return; }
            Race race = f1Data.getRace(index);
            event.replyEmbeds(EmbedCreator.createRace(race).build()).queue();
        }
        else if (event.getName().equals("nextrace")) {
            Race race = f1Data.getNextRace();
            event.replyEmbeds(EmbedCreator.createRace(race).build()).queue();
        }
        else if (event.getName().equals("driverstandings")) {
            event.replyEmbeds(EmbedCreator.createDriverStandings(f1Data.getDriverStandings()).build()).queue();
        }
        else if (event.getName().equals("constructorstandings")) {
            event.replyEmbeds(EmbedCreator.createConstructorStandings(f1Data.getConstructorStandings()).build()).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping", "check if don is sleeping :)"));
        commandData.add(Commands.slash("getrace", "nth race").addOption(OptionType.INTEGER, "racenumber", "Race to get info from", true, true));
        commandData.add(Commands.slash("nextrace", "Get the upcoming Grand Prix"));
        commandData.add(Commands.slash("driverstandings", "Get info on driver championship standings"));
        commandData.add(Commands.slash("constructorstandings", "Get info on constructor championship standings"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
