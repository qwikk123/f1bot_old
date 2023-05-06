package qwikk.f1bot.commands;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    private final F1Data f1Data;
    private LocalDateTime lastUpdate;
    private final CommandManager commandManager;

    public CommandListener(F1Data f1Data) {
        super();
        this.f1Data = f1Data;
        lastUpdate = LocalDateTime.now();
        commandManager = new CommandManager(f1Data);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (lastUpdate.plusHours(2).isBefore(LocalDateTime.now())) {
            f1Data.update();
            lastUpdate = LocalDateTime.now();
        }
        commandManager.getCommands().get(event.getName()).execute(event, f1Data);
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for(BotCommand command : commandManager.getCommandList()) {
            SlashCommandData scd = Commands.slash(command.name, command.description);
            if (command.hasOptions()) { scd.addOptions(command.optionList); }
            commandData.add(scd);
        }
        for (CommandData cd : commandData) {
            event.getGuild().upsertCommand(cd).queue();
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for(BotCommand command : commandManager.getCommandList()) {
            SlashCommandData scd = Commands.slash(command.name, command.description);
            if (command.hasOptions()) { scd.addOptions(command.optionList); }
            commandData.add(scd);
        }
        for (CommandData cd : commandData) {
            event.getGuild().upsertCommand(cd).queue();
        }
    }
}
