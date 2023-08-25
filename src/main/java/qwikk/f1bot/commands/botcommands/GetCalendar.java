package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import qwikk.f1bot.model.Race;
import qwikk.f1bot.utils.EmbedCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetCalendar extends BotCommand{
    private final List<Race> raceList;
    private static final int pageSize = 10;

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
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(Button.danger("prev-calendar", "Previous").asDisabled());
        buttonList.add(Button.danger("next-calendar", "Next"));
        event.getHook().sendMessageEmbeds(
                        EmbedCreator.createCalendar(raceList,0).build())
                .setActionRow(buttonList)
                .queue();
    }

    public void handleButtons(ButtonInteractionEvent event, String buttonId) {
        String pageNumber = Objects.requireNonNull(
                        event.getMessage().getEmbeds().get(0).getFooter().getText(), "calendar footer is null")
                .split("/")[0];

        int page = Integer.parseInt(pageNumber)-1;
        List<Button> buttonList = event.getMessage().getButtons().stream()
                .map(Button::asEnabled)
                .collect(Collectors.toList());

        if (buttonId.equals("next-calendar")) {
            page++;
            if ((page * pageSize) + pageSize >= raceList.size()) {
                buttonList.set(1, buttonList.get(1).asDisabled());
            }
        }
        else if (buttonId.equals("prev-calendar")) {
            page--;
            if (page == 0) {
                buttonList.set(0, buttonList.get(0).asDisabled());
            }
        }
        event.editMessageEmbeds(
                        EmbedCreator.createCalendar(raceList, page).build())
                .setActionRow(buttonList)
                .queue();
    }
}
