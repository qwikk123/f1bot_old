package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.commands.BotCommand;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

public class ConstructorStandings extends BotCommand {

    public ConstructorStandings(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().sendMessageEmbeds(EmbedCreator.createConstructorStandings(F1Data.getF1Data().getConstructorStandings())
                .build())
                .queue();
    }
}
