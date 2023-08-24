package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.model.Race;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UpcomingRaceMessage implements Runnable {

    private final List<TextChannel> channelList;
    private final LocalDateTime scheduledTime;
    private final Race nextRace;
    public UpcomingRaceMessage(List<TextChannel> channelList, LocalDateTime scheduledTime, Race nextRace) {
        this.channelList = channelList;
        this.scheduledTime = scheduledTime;
        this.nextRace = nextRace;
    }
    @Override
    public void run() {
        System.out.println("Scheduled at: "+scheduledTime+" Running at: "+LocalDateTime.now());

        InputStream inputStream = Objects.requireNonNull(
                getClass().getResourceAsStream(nextRace.getImagePath()), "inputStream is null");

        channelList.forEach(x ->
            x.sendMessageEmbeds(EmbedCreator.createUpcoming(nextRace).build())
                    .addFiles(FileUpload.fromData(inputStream, "circuitImage.png"))
                    .queue()
        );
    }
}
