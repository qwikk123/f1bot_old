package org.f1bot;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import org.f1bot.f1data.F1Data;
import org.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final F1Data f1Data;
    private LocalDateTime lastUpdate;

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
        // COMMAND /ping
        if (event.getName().equals("ping")) {
            long ping = event.getTimeCreated().until(OffsetDateTime.now(), ChronoUnit.MILLIS);
            event.reply("pong after: "+ping+" ms :)").queue();
        }

        // COMMAND /getrace [racenumber]
        else if (event.getName().equals("getrace")) {
            int index = event.getOption("racenumber").getAsInt()-1; //Non-null warning, but this will never be null as racenumber is required
            Race race = f1Data.getRace(index);
            String fileName = race.getCircuitName().replaceAll(" ","")+".png";
            String path = "assets/circuitimages/"+fileName;
            File f = new File(path);
            System.out.println(path);
            System.out.println(fileName);
            event.replyEmbeds(EmbedCreator.createRace(race).build()).addFiles(FileUpload.fromData(f,fileName)).queue();
        }

        // COMMAND /nextrace
        else if (event.getName().equals("nextrace")) {
            Race race = f1Data.getNextRace();
            String fileName = race.getCircuitName().replaceAll(" ","")+".png";
            String path = "assets/circuitimages/"+fileName;
            File f = new File(path);
            System.out.println(path);
            System.out.println(fileName);
            event.replyEmbeds(EmbedCreator.createRace(race).build()).addFiles(FileUpload.fromData(f,fileName)).queue();
        }

        // COMMAND /driverstandings
        else if (event.getName().equals("driverstandings")) {
            event.replyEmbeds(EmbedCreator.createDriverStandings(f1Data.getDriverStandings()).build()).queue();
        }

        // COMMAND /constructorstandings
        else if (event.getName().equals("constructorstandings")) {
            event.replyEmbeds(EmbedCreator.createConstructorStandings(f1Data.getConstructorStandings()).build()).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // COMMAND /ping
        commandData.add(Commands.slash(
                "ping",
                "ping the bot :)"));

        // COMMAND /getrace [racenumber]
        OptionData option1 = new OptionData(
                OptionType.INTEGER,
                "racenumber",
                "Race to get info from",
                true,
                true).setMinValue(1).setMaxValue(f1Data.getRaceList().size());
        commandData.add(Commands.slash(
                "getrace",
                "nth race").addOptions(option1));

        // COMMAND /nextrace
        commandData.add(Commands.slash(
                "nextrace",
                "Get the upcoming Grand Prix"));

        // COMMAND /driverstandings
        commandData.add(Commands.slash(
                "driverstandings",
                "Get info on driver championship standings"));

        // COMMAND /constructorstandings
        commandData.add(Commands.slash(
                "constructorstandings",
                "Get info on constructor championship standings"));


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
