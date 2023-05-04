package org.f1bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BotCommand {
    String name;
    String description;
    protected List<OptionData> optionList;

    public BotCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(@NotNull SlashCommandInteractionEvent event, F1Data f1data);
    public boolean hasOptions() {
        return optionList != null;
    }
}
