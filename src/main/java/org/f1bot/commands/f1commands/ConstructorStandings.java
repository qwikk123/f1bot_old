package org.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.f1bot.EmbedCreator;
import org.f1bot.commands.BotCommand;
import org.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

public class ConstructorStandings extends BotCommand {

    public ConstructorStandings(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, F1Data f1Data) {
        event.replyEmbeds(EmbedCreator.createConstructorStandings(f1Data.getConstructorStandings()).build()).queue();
    }
}
