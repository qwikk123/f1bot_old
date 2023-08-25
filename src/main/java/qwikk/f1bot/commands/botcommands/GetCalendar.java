package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import qwikk.f1bot.model.Race;
import qwikk.f1bot.utils.EmbedCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GetCalendar extends BotCommand{
    private final List<Race> raceList;
    private final HashMap<String, String> countryCodeMap;

    /**
     * Creates an instance of GetCalendar.
     *
     * @param name        This commands name
     * @param description This commands description
     * @param raceList List of races for the current F1 season
     */
    public GetCalendar(String name, String description, List<Race> raceList) {
        super(name, description);
        this.raceList = raceList;
        this.countryCodeMap = new HashMap<>();
        for (Locale iso : Locale.getAvailableLocales()) {
            countryCodeMap.put(iso.getDisplayCountry(), iso.getCountry());
        }
        countryCodeMap.put("UAE", "ae");
        countryCodeMap.put("USA", "us");
        countryCodeMap.put("UK", "gb");
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(
                        EmbedCreator.createCalendar(raceList, countryCodeMap).build())
                .queue();
    }
}
