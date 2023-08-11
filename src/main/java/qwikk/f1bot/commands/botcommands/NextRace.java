package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NextRace extends BotCommand {
    private final ArrayList<Race> raceList;

    public NextRace(String name, String description, ArrayList<Race> raceList) {
        super(name, description);
        this.raceList = raceList;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Race nextRace = getNextRace();
        URL img = getClass().getClassLoader().getResource("/circuitimages/"+ nextRace.getImageName());
        String imgPath = URLDecoder.decode(img.getPath(), StandardCharsets.UTF_8);
        File f = new File(imgPath);
        event.getHook().sendMessageEmbeds(EmbedCreator.createRace(nextRace).build())
                .addFiles(FileUpload.fromData(f, "circuitImage.png"))
                .queue();
    }

    private Race getNextRace() {
        for (Race r : raceList) {
            if (r.getLocalDateTime().isAfter(LocalDateTime.now())) { return r; }
        }
        return null;
    }
}
