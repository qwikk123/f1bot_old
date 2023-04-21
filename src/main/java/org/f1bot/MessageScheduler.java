package org.f1bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageScheduler {
    F1Data f1Data;
    JDA bot;
    ScheduledExecutorService executorService;
    RaceStartingMessage nextRaceStarting;
    public MessageScheduler(JDA bot, F1Data f1Data) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.bot = bot;
        this.f1Data = f1Data;
    }

    public void schedule() {
        Race nextRace = f1Data.getNextRace();
        TextChannel channel = bot.getTextChannelById("1091044495023407174");

        nextRaceStarting = new RaceStartingMessage(channel, LocalDateTime.now(), nextRace);
        System.out.println("SCHEDULED FOR: "+nextRace.localDateTime.minusDays(2));

        executorService.schedule(
                nextRaceStarting,
                LocalDateTime.now().until(nextRace.localDateTime.minusDays(2), ChronoUnit.MINUTES),
                TimeUnit.MINUTES);
    }
}
