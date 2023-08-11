package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.Race;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GetRace extends BotCommand {

    ArrayList<Race> raceList;

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

        int index = event.getOption("racenumber").getAsInt()-1; //Non-null warning, but this will never be null as racenumber is required
        Race race = raceList.get(index);
        InputStream inputStream = getClass().getResourceAsStream("/circuitimages/"+ race.getImageName());

        WebhookMessageCreateAction<Message> action = event.getHook().sendMessageEmbeds(EmbedCreator.createRace(race).build())
                .addFiles(FileUpload.fromData(inputStream, "circuitImage.png"));
        if (race.hasRaceResult()) {
            action.addActionRow(buttonList);
        }
        action.queue();
    }
}
