package org.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import org.f1bot.EmbedCreator;
import org.f1bot.commands.BotCommand;
import org.f1bot.f1data.F1Data;
import org.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.io.File;
import java.util.ArrayList;

public class GetRace extends BotCommand {

    public GetRace(String name, String description, F1Data f1Data) {
        super(name, description);
        optionList = new ArrayList<>();
        optionList.add(new OptionData(
                OptionType.INTEGER,
                "racenumber",
                "Race to get info from",
                true,
                true).setMinValue(1).setMaxValue(f1Data.getRaceList().size()));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, F1Data  f1Data) {
        int index = event.getOption("racenumber").getAsInt()-1; //Non-null warning, but this will never be null as racenumber is required
        Race race = f1Data.getRace(index);
        File f = new File(race.getImagePath());
        event.replyEmbeds(EmbedCreator.createRace(race).build()).addFiles(FileUpload.fromData(f, race.getImageName())).queue();
    }
}
