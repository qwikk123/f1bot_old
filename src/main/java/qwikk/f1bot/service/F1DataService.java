package qwikk.f1bot.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import qwikk.f1bot.commands.listeners.CommandListener;
import qwikk.f1bot.datasource.F1DataSource;
import qwikk.f1bot.model.Constructor;
import qwikk.f1bot.model.Driver;
import qwikk.f1bot.model.Race;
import qwikk.f1bot.scheduling.MessageScheduler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class F1DataService {
    private final MessageScheduler messageScheduler;
    private static final String scheduledTextChannel = "f1";
    private final JDA bot;
    private final CommandListener commandListener;
    private final F1DataSource dataSource;
    private Race nextRace;

    /**
     * Creates an F1DataService and initializes its F1DataSource and MessageScheduler.
     * Then it tells the dataSource to set its data.
     * @param bot an instance of a JDA bot
     * @param commandListener an instance of a CommandListener
     */
    public F1DataService(JDA bot, CommandListener commandListener) {
        this.commandListener = commandListener;
        dataSource = new F1DataSource();
        this.bot = bot;
        messageScheduler = new MessageScheduler(bot.getTextChannelsByName(scheduledTextChannel,true));
        setData();
    }

    /**
     * setData() will request an F1DataSource to update its data.
     * If the dataSource is updated the service will update its nextRace and
     * tell the messageScheduler to reschedule the new race
     */
    public void setData() {
        boolean updated = dataSource.setData();

        if (updated) {
            setNextRace(dataSource.retrieveRaceList());
            if (commandListener.isReady()) commandListener.upsertCommands(bot.getGuilds());
        }
        updateTextChannelDescription();
    }

    /**
     * Finds the next race from today in raceList, updates nextRace and tells the scheduler to refresh itself.
     * @param raceList List of races in the current F1 season.
     */
    public void setNextRace(List<Race> raceList) {
        for (Race r : raceList) {
            if (r.getRaceInstant().isAfter(Instant.now())) {
                nextRace = r;
                refreshScheduler();
                return;
            }
        }
    }

    /**
     * Refreshes the messageScheduler if the current time is before its scheduled datetime
     * (The scheduled message datetime is always 2 days before the race date)
     */
    public void refreshScheduler() {
        if (nextRace.getUpcomingDate().isAfter(Instant.now())) {
            messageScheduler.setChannelList(bot.getTextChannelsByName(scheduledTextChannel,true));
            messageScheduler.cancel();
            messageScheduler.schedule(nextRace);
        }
    }

    /**
     * Method that sets a text channel description.
     */
    public void updateTextChannelDescription() {
        List<TextChannel> textChannels = bot.getTextChannelsByName(scheduledTextChannel,true);
        for (TextChannel textChannel : textChannels) {
            textChannel.getManager().setTopic("Everything Formula 1 | Next race: "+nextRace.getRaceRelativeTimestamp()).queue();
        }
    }

    public ArrayList<Race> getRaceList() { return dataSource.retrieveRaceList(); }
    public HashMap<String, Driver> getDriverMap() { return dataSource.retrieveDriverMap(); }
    public ArrayList<Constructor> getConstructorStandings() { return dataSource.retrieveConstructorStandings(); }
}