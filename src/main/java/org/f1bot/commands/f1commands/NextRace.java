package org.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.f1bot.EmbedCreator;
import org.f1bot.commands.BotCommand;
import org.f1bot.f1data.F1Data;
import org.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class NextRace extends BotCommand {

    public NextRace(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, F1Data f1Data) {
        Race race = f1Data.getNextRace();
        File f = new File(race.getImagePath());
        event.replyEmbeds(EmbedCreator.createRace(race).build()).addFiles(FileUpload.fromData(f, race.getImageName())).queue();
    }
}
