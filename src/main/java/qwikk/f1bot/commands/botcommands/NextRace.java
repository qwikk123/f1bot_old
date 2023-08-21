package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
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
        InputStream inputStream = getClass().getResourceAsStream("/circuitimages/"+ nextRace.getImageName());

        event.getHook().sendMessageEmbeds(EmbedCreator.createRace(nextRace).build())
                .addFiles(FileUpload.fromData(inputStream, "circuitImage.png"))
                .queue();
    }

    private Race getNextRace() {
        for (Race r : raceList) {
            if (r.getLocalDateTime().isAfter(LocalDateTime.now())) { return r; }
        }
        return null;
    }
}
