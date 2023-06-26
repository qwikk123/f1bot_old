package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import qwikk.f1bot.f1data.Constructor;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.commands.BotCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConstructorStandings extends BotCommand {
    ArrayList<Constructor> constructorStandings;

    public ConstructorStandings(String name, String description, ArrayList<Constructor> constructorStandings) {
        super(name, description);
        this.constructorStandings = constructorStandings;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(EmbedCreator.createConstructorStandings(constructorStandings)
                .build())
                .queue();
    }
}
