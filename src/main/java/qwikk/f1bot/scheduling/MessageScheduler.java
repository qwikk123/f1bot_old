package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import qwikk.f1bot.model.Race;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MessageScheduler {
    private List<TextChannel> channelList;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> upcomingRaceFuture;

    public MessageScheduler(List<TextChannel> channelList) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        this.channelList = channelList;
    }

    public void schedule(Race nextRace) {
        UpcomingRaceMessage upcomingRaceMessage = new UpcomingRaceMessage(channelList, LocalDateTime.now(), nextRace);
        System.out.println("SCHEDULED TASK FOR: "+nextRace.getUpcomingDate()+"\n");

        upcomingRaceFuture = executorService.schedule(
                upcomingRaceMessage,
                LocalDateTime.now().until(nextRace.getUpcomingDate(), ChronoUnit.MINUTES),
                TimeUnit.MINUTES);
    }
    public void cancel() {
        if (upcomingRaceFuture != null) upcomingRaceFuture.cancel(true);
    }

    public void setChannelList(List<TextChannel> channelList) {
        this.channelList = channelList;
    }
}
