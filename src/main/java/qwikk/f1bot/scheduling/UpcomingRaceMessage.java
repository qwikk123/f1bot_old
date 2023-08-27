package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.model.Race;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Class representing an upcoming race message.
 */
public class UpcomingRaceMessage implements Runnable {

    private final List<TextChannel> channelList;
    private final LocalDateTime scheduledTime;
    private final Race nextRace;

    /**
     * Creates an instance of UpcomingRaceMessage and initializes its variables.
     * @param channelList List of text channels to send messages in
     * @param scheduledTime Time for when the messages should be sent
     * @param nextRace The race to be included in the message
     */
    public UpcomingRaceMessage(List<TextChannel> channelList, LocalDateTime scheduledTime, Race nextRace) {
        this.channelList = channelList;
        this.scheduledTime = scheduledTime;
        this.nextRace = nextRace;
    }

    /**
     * Method that runs the scheduled task.
     * Sends an upcoming race message to every text channel in the channelList.
     */
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
