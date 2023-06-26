package qwikk.f1bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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

    public abstract void execute(@NotNull SlashCommandInteractionEvent event);
    public boolean hasOptions() {
        return optionList != null;
    }
}
