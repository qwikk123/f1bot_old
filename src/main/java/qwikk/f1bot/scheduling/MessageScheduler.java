package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import qwikk.f1bot.f1data.Race;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MessageScheduler {
    private final TextChannel channel;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> upcomingRaceFuture;

    public MessageScheduler(TextChannel channel) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.channel = channel;
    }

    public void schedule(Race nextRace) {
        UpcomingRaceMessage nextRaceStarting = new UpcomingRaceMessage(channel, LocalDateTime.now(), nextRace);
        System.out.println("SCHEDULED FOR: "+nextRace.getLocalDateTime().minusDays(2));

        upcomingRaceFuture = executorService.schedule(
                nextRaceStarting,
                LocalDateTime.now().until(nextRace.getLocalDateTime().minusDays(2), ChronoUnit.MINUTES),
                TimeUnit.MINUTES);
    }
    public void cancel() {
        if (upcomingRaceFuture != null) upcomingRaceFuture.cancel(true);
    }
}
