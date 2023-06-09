package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.commands.BotCommand;
import qwikk.f1bot.f1data.F1Data;
import qwikk.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        URL img = getClass().getResource("/circuitimages/"+ race.getImageName());
        String imgPath = URLDecoder.decode(img.getPath(), StandardCharsets.UTF_8);
        File f = new File(imgPath);
        event.getHook().sendMessageEmbeds(EmbedCreator.createRace(race).build())
                .addFiles(FileUpload.fromData(f, "circuitImage.png"))
                .queue();
    }
}
