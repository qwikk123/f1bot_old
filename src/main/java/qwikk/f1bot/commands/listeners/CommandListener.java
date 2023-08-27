package qwikk.f1bot.commands.listeners;

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
import qwikk.f1bot.commands.CommandManager;
import qwikk.f1bot.commands.botcommands.BotCommand;
import qwikk.f1bot.commands.botcommands.DriverStandings;
import qwikk.f1bot.commands.botcommands.GetRace;
import qwikk.f1bot.service.F1DataService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for managing EventListeners relating to BotCommands and their buttons.
 * The class extends the JDA class ListenerAdapter
 */
public class CommandListener extends ListenerAdapter {
    private final CommandManager commandManager;
    private final F1DataService f1DataService;
    private boolean ready = false;

    /**
     * Creates an instance of CommandListener and initializes the F1DataService and CommandManager.
     * @param bot The instance of this programs JDA bot.
     */
    public CommandListener(JDA bot) {
        super();
        this.f1DataService = new F1DataService(bot, this);
        commandManager = new CommandManager(f1DataService);
    }

    /**
     * Method to handle SlashCommands (/commandName) from Discord.
     * @param event event coming from Discord.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        f1DataService.setData();
        commandManager.getCommands().get(event.getName()).execute(event);
    }

    /**
     * Method that sets the boolean ready to true.
     * It runs on the bots ReadyEvent.
     * This is to stop the Listener from updating commands before the CommandManager and F1DataService is ready.
     * It is initialized in the Main method through upsertCommands()
     * @param event event coming from Discord.
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        ready = true;
    }

    /**
     * Method to update commands when the bot joins a new server.
     * @param event event coming from Discord.
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        ArrayList<Guild> guildList = new ArrayList<>();
        guildList.add(event.getGuild());
        upsertCommands(guildList);
    }

    /**
     * Method to update or create new commands on a discord server.
     * @param guilds List of servers to add/update commands for.
     */
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

    /**
     * Method handling button interactions it calls the specific commands button methods.
     * @param event event coming from Discord.
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = Objects.requireNonNull(event.getButton().getId(), "buttonId is null");
        String buttonType = buttonId.split("-")[1];

        switch (buttonType) {
            case "dstandings" -> {
                BotCommand command = commandManager.getCommands().get("driverstandings");
                if (command instanceof DriverStandings) {
                    ((DriverStandings) command).handleButtons(event, buttonId);
                }
            }
            case "getrace", "resultpage" -> {
                BotCommand command = commandManager.getCommands().get("getrace");
                if (command instanceof GetRace) {
                    ((GetRace) command).handleButtons(event, buttonId, buttonType, f1DataService.getDriverMap());
                }
            }
        }
    }

    public boolean isReady() {return ready;}
}
