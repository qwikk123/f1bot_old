package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.Race;

import java.io.File;
import java.time.LocalDateTime;

public class UpcomingRaceMessage implements Runnable{

    private final TextChannel channel;
    private final LocalDateTime scheduledTime;
    private final Race nextRace;
    public UpcomingRaceMessage(TextChannel channel, LocalDateTime scheduledTime, Race nextRace) {
        this.channel = channel;
        this.scheduledTime = scheduledTime;
        this.nextRace = nextRace;
    }
    @Override
    public void run() {
        System.out.println("Scheduled at: "+scheduledTime+" Running at: "+LocalDateTime.now());
        File file = new File(nextRace.getImagePath());
        channel.sendMessageEmbeds(EmbedCreator.createUpcoming(nextRace).build()).addFiles(FileUpload.fromData(file, nextRace.getImageName())).queue();
    }
}
