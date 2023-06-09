package qwikk.f1bot.commands;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {
    private final F1Data f1Data;
    private final CommandManager commandManager;

    public CommandListener(F1Data f1Data) {
        super();
        this.f1Data = f1Data;
        commandManager = new CommandManager(f1Data);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        f1Data.update();
        event.deferReply().queue();
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

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        int pageSize = 5;
        String buttonId = event.getButton().getId();
        String buttonType = buttonId.split("-")[1];
        if (buttonType.equals("dstandings")) {
            int page = Integer.parseInt(event.getMessage().getEmbeds().get(0).getFooter().getText().split("/")[0])-1;
            List<Button> buttonList = event.getMessage().getButtons().stream()
                    .map(Button::asEnabled)
                    .collect(Collectors.toList());

            if (buttonId.equals("next-dstandings")) {
                page++;
                if ((page * pageSize) + pageSize >= f1Data.getDriverMap().size()) {
                    buttonList.set(1, buttonList.get(1).asDisabled());
                }
            } else if (buttonId.equals("prev-dstandings")) {
                page--;
                if (page == 0) {
                    buttonList.set(0, buttonList.get(0).asDisabled());
                }
            }
            event.editMessageEmbeds(
                            EmbedCreator.createDriverStandings(f1Data.getDriverMap(), page).build())
                    .setActionRow(buttonList)
                    .queue();
        }
    }
}
