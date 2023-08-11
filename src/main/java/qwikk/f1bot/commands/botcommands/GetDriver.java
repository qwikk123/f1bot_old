package qwikk.f1bot.commands.botcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class GetDriver extends BotCommand {

    HashMap<String, Driver> driverMap;
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
        String driverId = event.getOption("drivername").getAsString(); //Non-null warning, but this will never be null as racenumber is required
        Driver driver = driverMap.get(driverId);
        InputStream inputStream = getClass().getResourceAsStream("/driverimages/"+driver.code()+".png");

        event.getHook().sendMessageEmbeds(EmbedCreator.createDriverProfile(driver).build())
                .addFiles(FileUpload.fromData(inputStream, "driverImage.png"))
                .queue();
    }
}
