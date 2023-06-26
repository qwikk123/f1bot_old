package qwikk.f1bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

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
