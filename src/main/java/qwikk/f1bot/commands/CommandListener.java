package qwikk.f1bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import qwikk.f1bot.commands.botcommands.BotCommand;
import qwikk.f1bot.commands.botcommands.DriverStandings;
import qwikk.f1bot.commands.botcommands.GetRace;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    private final CommandManager commandManager;
    private final F1Data f1data;
    private boolean ready = false;

    public CommandListener(JDA bot) {
        super();
        this.f1data = new F1Data(bot, this);
        commandManager = new CommandManager(f1data);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        f1data.setData();
        commandManager.getCommands().get(event.getName()).execute(event);
    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        ready = true;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        ArrayList<Guild> guildList = new ArrayList<>();
        guildList.add(event.getGuild());
        upsertCommands(guildList);
    }

    public void upsertCommands(List<Guild> guilds) {
        List<CommandData> commandData = new ArrayList<>();
        for(BotCommand command : commandManager.getCommandList()) {
            SlashCommandData scd = Commands.slash(command.getName(), command.getDescription());
            if (command.hasOptions()) { scd.addOptions(command.getOptions()); }
            commandData.add(scd);
        }
        for (CommandData cd : commandData) {
            for (Guild g : guilds) {
                g.upsertCommand(cd).queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String buttonType = buttonId.split("-")[1];

        if (buttonType.equals("dstandings")) {
            BotCommand command = commandManager.getCommands().get("driverstandings");
            if (command instanceof DriverStandings) {
                ((DriverStandings) command).handleButtons(event, buttonId);
            }
        }
        else if (buttonType.equals("getrace") || buttonType.equals("resultpage")) {
            BotCommand command = commandManager.getCommands().get("getrace");
            if (command instanceof GetRace) {
                ((GetRace) command).handleButtons(event, buttonId, buttonType, f1data.getDriverMap());
            }
        }
    }

    public boolean isReady() {return ready;}
}
