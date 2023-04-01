package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;
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
            event.reply("pong").queue();
        }
        else if (event.getName().equals("deferping")) {
            event.deferReply().queue();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            event.getHook().sendMessage("pong").queue();
        }
        else if (event.getName().equals("getrace")) {
            int index = event.getOption("racenumber").getAsInt()-1;
            if (!f1Data.hasRace(index)) {event.reply("invalid racenumber: "+index+1).queue();return;}
            Race race = f1Data.getRace(index);
            event.replyEmbeds(createEmbed(race).build()).queue();
        }
        else if (event.getName().equals("nextrace")) {
            Race race = f1Data.getNextRace();
            event.replyEmbeds(createEmbed(race).build()).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("ping", "check if don is sleeping :)"));
        commandData.add(Commands.slash("deferping", "check if don is sleeping, but let him think first :)"));
        commandData.add(Commands.slash("getrace", "nth race").addOption(OptionType.INTEGER, "racenumber", "Race to get info from", true, true));
        commandData.add(Commands.slash("nextrace", "Get the upcoming Grand Prix"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    public EmbedBuilder createEmbed(Race r) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(r.name);
        eb.setColor(Color.BLUE);
        eb.addField("Race: ", r.getRaceDateAsString(),true);
        eb.addField("Qualifying: ", r.getQualifyingDateAsString(),true);
        return eb;
    }
}
