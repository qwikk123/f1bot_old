package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class representing the /getdriver command.
 */
public class GetDriver extends BotCommand {

    HashMap<String, Driver> driverMap;

    /**
     * Creates an instance of GetDriver.
     * @param name This commands name
     * @param description This commands description
     * @param driverMap Map containing the drivers from this F1 season.
     */
    public GetDriver(String name, String description, HashMap<String, Driver> driverMap) {
        super(name, description);
        this.driverMap = driverMap;
        optionList = new ArrayList<>();
        ArrayList<Command.Choice> choiceList= new ArrayList<>();
        driverMap.values().forEach(x -> choiceList.add(new Command.Choice(x.name(),x.driverId())));
        optionList.add(new OptionData(
                OptionType.STRING,
                "drivername",
                "Select driver to get info from",
                true,
                false).addChoices(choiceList));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String driverId = Objects.requireNonNull(event.getOption("drivername")).getAsString();
        Driver driver = driverMap.get(driverId);
        InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/driverimages/"+driver.code()+".png"), "inputStream is null");

        event.getHook().sendMessageEmbeds(EmbedCreator.createDriverProfile(driver).build())
                .addFiles(FileUpload.fromData(inputStream, "driverImage.png"))
                .queue();
    }
}
