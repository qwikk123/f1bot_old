package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing the /driverstandings command.
 */
public class DriverStandings extends BotCommand {

    private final HashMap<String, Driver> driverMap;
    private static final int pageSize = 7;

    /**
     * Creates an instance of DriverStandings.
     * @param name This commands name
     * @param description This commands description
     * @param driverMap Map containing the drivers from this F1 season.
     */
    public DriverStandings(String name, String description, HashMap<String, Driver> driverMap) {
        super(name, description);
        this.driverMap = driverMap;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(Button.danger("prev-dstandings", "Previous").asDisabled());
        buttonList.add(Button.danger("next-dstandings", "Next"));
        event.getHook().sendMessageEmbeds(
                EmbedCreator.createDriverStandings(driverMap,0, pageSize).build())
                .setActionRow(buttonList)
                .queue();
    }


    /**
     * Method to handle the buttons for this bot command.
     * @param event event from Discord.
     * @param buttonId The pressed buttons id
     */
    public void handleButtons(ButtonInteractionEvent event, String buttonId) {
        String pageNumber = Objects.requireNonNull(
                        event.getMessage().getEmbeds().get(0).getFooter().getText(), "driverstandings footer is null")
                        .split("/")[0];

        int page = Integer.parseInt(pageNumber)-1;
        List<Button> buttonList = event.getMessage().getButtons().stream()
                .map(Button::asEnabled)
                .collect(Collectors.toList());

        switch (buttonId) {
            case "next-dstandings" -> {
                page++;
                if ((page * pageSize) + pageSize >= driverMap.size()) {
                    buttonList.set(1, buttonList.get(1).asDisabled());
                }
            }
            case "prev-dstandings" -> {
                page--;
                if (page == 0) {
                    buttonList.set(0, buttonList.get(0).asDisabled());
                }
            }
        }
        event.editMessageEmbeds(
                        EmbedCreator.createDriverStandings(driverMap, page, pageSize).build())
                .setActionRow(buttonList)
                .queue();
    }
}
