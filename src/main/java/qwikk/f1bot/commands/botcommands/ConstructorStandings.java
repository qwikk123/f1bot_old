package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import qwikk.f1bot.model.Constructor;
import qwikk.f1bot.utils.EmbedCreator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Class representing the /constructorstandings command.
 */
public class ConstructorStandings extends BotCommand {
    ArrayList<Constructor> constructorStandings;

    /**
     * Creates an instance of ConstructorStandings.
     * @param name This commands name
     * @param description This commands description
     * @param constructorStandings List of the current constructors standings in the F1 season
     */
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
