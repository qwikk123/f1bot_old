package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.model.Race;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class representing the /getrace command.
 */
public class GetRace extends BotCommand {

    private final ArrayList<Race> raceList;
    private static final int pageSize = 10;

    /**
     * Creates an instance of GetRace.
     *
     * @param name           This commands name
     * @param description    This commands description
     * @param raceList       A list of races in this F1 season
     */
    public GetRace(String name, String description, ArrayList<Race> raceList) {
        super(name, description);
        this.raceList = raceList;
        optionList = new ArrayList<>();
        optionList.add(new OptionData(
                OptionType.INTEGER,
                "racenumber",
                "Race to get info from",
                true,
                true).setMinValue(1).setMaxValue(raceList.size()));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(Button.danger("info-getrace", "Info").asDisabled());
        buttonList.add(Button.danger("result-getrace", "Result"));

        int index = Objects.requireNonNull(
                event.getOption("racenumber"), "racenumber option is null")
                .getAsInt()-1;
        Race race = raceList.get(index);
        InputStream inputStream = Objects.requireNonNull(
                getClass().getResourceAsStream(race.getImagePath()), "inputStream is null");

        WebhookMessageCreateAction<Message> action = event.getHook().sendMessageEmbeds(EmbedCreator.createRace(race).build())
                .addFiles(FileUpload.fromData(inputStream, "circuitImage.png"));
        if (race.hasRaceResult()) {
            action.addActionRow(buttonList);
        }
        action.queue();
    }

    /**
     * Method for handling button presses for this command.
     * @param event event from Discord.
     * @param buttonId The id for the pressed button
     * @param buttonType The type for the pressed button
     * @param driverMap Map containing the drivers from this F1 season.
     */
    public void handleButtons(@NotNull ButtonInteractionEvent event, String buttonId, String buttonType, HashMap<String, Driver> driverMap) {
        List<Button> buttonList = event.getMessage().getButtons().stream()
                .map(Button::asEnabled)
                .collect(Collectors.toList());
        String title = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getTitle(), "title is null");
        Matcher matcher = Pattern.compile("\\d+").matcher(title);
        if (!matcher.find()) { System.out.println("Invalid pattern in getrace"); return; }
        int index = Integer.parseInt(matcher.group(0))-1;
        Race race = raceList.get(index);

        if (buttonId.equals("info-getrace")) {
            clickInfo(buttonList, event, race);
        }
        else if (buttonId.equals("result-getrace")) {
            clickResult(buttonList, event, race, driverMap);
        }
        else if (buttonType.equals("resultpage")) {
            clickResultPage(buttonList, event, race, driverMap, buttonId);
        }
    }

    /**
     * Method to handle the press of the Info button.
     * @param buttonList List of buttons from the current Discord event
     * @param event event from Discord
     * @param race Current race to get info from
     */
    public void clickInfo(List<Button> buttonList, ButtonInteractionEvent event, Race race) {
        buttonList.set(0, buttonList.get(0).asDisabled());

        InputStream inputStream = Objects.requireNonNull(
                getClass().getResourceAsStream(race.getImagePath()), "inputStream is null");

        event.editMessageEmbeds(EmbedCreator.createRace(race).build())
                .setFiles(FileUpload.fromData(inputStream, "circuitImage.png"))
                .setActionRow(buttonList.subList(0, 2))
                .queue();
    }

    /**
     * Method to handle the press of the Result button.
     * @param buttonList List of buttons from the current Discord event
     * @param event event from Discord
     * @param race Current race to get info from
     * @param driverMap Map containing the drivers from this F1 season.
     */
    public void clickResult(List<Button> buttonList, ButtonInteractionEvent event, Race race, HashMap<String, Driver> driverMap) {
        buttonList.set(1, buttonList.get(1).asDisabled());
        buttonList.add(Button.danger("prev-resultpage", "Prev").asDisabled());
        buttonList.add(Button.danger("next-resultpage", "Next"));

        event.editMessageEmbeds(EmbedCreator.createRaceResult(race,driverMap,0, pageSize).build())
                .setActionRow(buttonList)
                .setReplace(true)
                .queue();
    }

    /**
     * Method for handling paging for the race result table.
     * @param buttonList List of buttons from the current Discord event
     * @param event event from Discord
     * @param race Current race to get info from
     * @param driverMap Map containing the drivers from this F1 season.
     * @param buttonId Id for the pressed button
     */
    public void clickResultPage(List<Button> buttonList, ButtonInteractionEvent event, Race race, HashMap<String, Driver> driverMap, String buttonId) {
        String footer = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter().getText(), "footer is null");
        int page = Integer.parseInt(footer.split("/")[0])-1;
        int maxPage = Integer.parseInt(footer.split("/")[1]);

        switch (buttonId) {
            case "next-resultpage" -> {
                page++;
                if ((page * pageSize) + pageSize >= maxPage) {
                    buttonList.set(3, buttonList.get(3).asDisabled());
                }
            }
            case "prev-resultpage" -> {
                page--;
                if (page == 0) {
                    buttonList.set(2, buttonList.get(2).asDisabled());
                }
            }
        }
        buttonList.set(1, buttonList.get(1).asDisabled());
        event.editMessageEmbeds(EmbedCreator.createRaceResult(race,driverMap,page, pageSize).build())
                .setActionRow(buttonList)
                .queue();
    }
}
