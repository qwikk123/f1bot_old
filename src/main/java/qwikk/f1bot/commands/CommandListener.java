package qwikk.f1bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.commands.botcommands.BotCommand;
import qwikk.f1bot.f1data.Race;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandListener extends ListenerAdapter {
    static int pageSize = 5;
    private final CommandManager commandManager;
    private final F1Data f1data;

    public CommandListener(JDA bot) {
        super();
        this.f1data = new F1Data(bot);
        commandManager = new CommandManager(f1data);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        f1data.setData();
        commandManager.getCommands().get(event.getName()).execute(event);
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for(BotCommand command : commandManager.getCommandList()) {
            SlashCommandData scd = Commands.slash(command.getName(), command.getDescription());
            if (command.hasOptions()) { scd.addOptions(command.getOptions()); }
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
            SlashCommandData scd = Commands.slash(command.getName(), command.getDescription());
            if (command.hasOptions()) { scd.addOptions(command.getOptions()); }
            commandData.add(scd);
        }
        for (CommandData cd : commandData) {
            event.getGuild().upsertCommand(cd).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String buttonType = buttonId.split("-")[1];

        if (buttonType.equals("dstandings")) {
            driverStandingsButton(event,buttonId);
        }
        else if (buttonType.equals("getrace") || buttonType.equals("resultpage")) {
            getRaceButton(event, buttonId, buttonType);
        }
    }

    private void driverStandingsButton(ButtonInteractionEvent event, String buttonId) {
        int page = Integer.parseInt(event.getMessage().getEmbeds().get(0).getFooter().getText().split("/")[0])-1;
        List<Button> buttonList = event.getMessage().getButtons().stream()
                .map(Button::asEnabled)
                .collect(Collectors.toList());

        if (buttonId.equals("next-dstandings")) {
            page++;
            if ((page * pageSize) + pageSize >= f1data.getDriverMap().size()) {
                buttonList.set(1, buttonList.get(1).asDisabled());
            }
        } else if (buttonId.equals("prev-dstandings")) {
            page--;
            if (page == 0) {
                buttonList.set(0, buttonList.get(0).asDisabled());
            }
        }
        event.editMessageEmbeds(
                        EmbedCreator.createDriverStandings(f1data.getDriverMap(), page).build())
                .setActionRow(buttonList)
                .queue();
    }

    private void getRaceButton (ButtonInteractionEvent event, String buttonId, String buttonType) {
        List<Button> buttonList = event.getMessage().getButtons().stream()
                .map(Button::asEnabled)
                .collect(Collectors.toList());
        Matcher matcher = Pattern.compile("\\d+").matcher(event.getMessage().getEmbeds().get(0).getTitle());
        if (!matcher.find()) { System.out.println("Invalid pattern in getrace"); return; }
        int index = Integer.parseInt(matcher.group(0))-1;
        Race race = f1data.getRace(index);

        if (buttonId.equals("info-getrace")) {
            buttonList.set(0, buttonList.get(0).asDisabled());

            URL img = getClass().getResource("/circuitimages/"+ race.getImageName());
            String imgPath = URLDecoder.decode(img.getPath(), StandardCharsets.UTF_8);
            File f = new File(imgPath);

            event.editMessageEmbeds(EmbedCreator.createRace(race).build())
                    .setFiles(FileUpload.fromData(f, "circuitImage.png"))
                    .setActionRow(buttonList.subList(0, 2))
                    .queue();
        }
        else if (buttonId.equals("result-getrace")) {
            buttonList.set(1, buttonList.get(1).asDisabled());
            buttonList.add(Button.danger("prev-resultpage", "Prev").asDisabled());
            buttonList.add(Button.danger("next-resultpage", "Next"));

            event.editMessageEmbeds(EmbedCreator.createRaceResult(race,f1data.getDriverMap(),0).build())
                    .setActionRow(buttonList)
                    .setReplace(true)
                    .queue();
        }
        else if (buttonType.equals("resultpage")) {
            String footer = event.getMessage().getEmbeds().get(0).getFooter().getText();
            int page = Integer.parseInt(footer.split("/")[0])-1;
            int maxPage = Integer.parseInt(footer.split("/")[1]);

            if (buttonId.equals("next-resultpage")) {
                page++;
                if ((page * pageSize) + pageSize >= maxPage) {
                    buttonList.set(3, buttonList.get(3).asDisabled());
                }
            } else if (buttonId.equals("prev-resultpage")) {
                page--;
                if (page == 0) {
                    buttonList.set(2, buttonList.get(2).asDisabled());
                }
            }
            buttonList.set(1, buttonList.get(1).asDisabled());
            event.editMessageEmbeds(EmbedCreator.createRaceResult(race,f1data.getDriverMap(),page).build())
                    .setActionRow(buttonList)
                    .queue();
        }
    }
}
