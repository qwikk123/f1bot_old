package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BotCommand {
    private final String name;
    private final String description;
    protected List<OptionData> optionList;

    public BotCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(@NotNull SlashCommandInteractionEvent event);
    public boolean hasOptions() {
        return optionList != null;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<OptionData> getOptions() { return optionList; }
    public void setOptions(List<OptionData> optionList) { this.optionList = optionList; }
}
