package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.commands.BotCommand;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class DriverStandings extends BotCommand {

    HashMap<String, Driver> driverMap;
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
                EmbedCreator.createDriverStandings(driverMap,0).build())
                .setActionRow(buttonList)
                .queue();
    }
}
