package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract class for bot commands.
 */
public abstract class BotCommand {
    private final String name;
    private final String description;
    protected List<OptionData> optionList;

    /**
     * Creates an instance of this command.
     * @param name This commands name
     * @param description This commands description
     */
    public BotCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Method to execute the command.
     * @param event event coming from Discord.
     */
    public abstract void execute(@NotNull SlashCommandInteractionEvent event);
    public boolean hasOptions() {
        return optionList != null;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<OptionData> getOptions() { return optionList; }
}
