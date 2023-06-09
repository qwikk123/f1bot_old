package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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

public class NextRace extends BotCommand {

    public NextRace(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, F1Data f1Data) {
        Race race = f1Data.getNextRace();
        URL img = getClass().getResource("/circuitimages/"+ race.getImageName());
        String imgPath = URLDecoder.decode(img.getPath(), StandardCharsets.UTF_8);
        File f = new File(imgPath);
        event.getHook().sendMessageEmbeds(EmbedCreator.createRace(race).build())
                .addFiles(FileUpload.fromData(f, "circuitImage.png"))
                .queue();
    }
}
