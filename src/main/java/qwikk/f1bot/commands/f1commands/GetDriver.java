package qwikk.f1bot.commands.f1commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import qwikk.f1bot.f1data.Driver;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.commands.BotCommand;
import qwikk.f1bot.f1data.F1Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GetDriver extends BotCommand {

    public GetDriver(String name, String description, F1Data f1Data) {
        super(name, description);
        optionList = new ArrayList<>();
        ArrayList<Command.Choice> choiceList= new ArrayList<>();
        f1Data.getDriverMap().values().forEach(x -> choiceList.add(new Command.Choice(x.name(),x.driverId())));
        optionList.add(new OptionData(
                OptionType.STRING,
                "drivername",
                "Select driver to get info from",
                true,
                false).addChoices(choiceList));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event, F1Data  f1Data) {
        String code = event.getOption("drivername").getAsString(); //Non-null warning, but this will never be null as racenumber is required
        System.out.println(code);
        Driver driver = f1Data.getDriver(code);
        event.replyEmbeds(EmbedCreator.createDriverProfile(driver).build()).queue();
    }
}
