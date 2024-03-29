package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class representing the /ping command.
 */
public class Ping extends BotCommand {
    public Ping(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        long ping = event.getTimeCreated().until(OffsetDateTime.now(), ChronoUnit.MILLIS);
        event.getHook().sendMessage("pong after: "+ping+" ms :)").queue();
    }
}
