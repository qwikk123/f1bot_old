package qwikk.f1bot.scheduling;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import qwikk.f1bot.utils.EmbedCreator;
import qwikk.f1bot.f1data.Race;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class UpcomingRaceMessage implements Runnable{

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

        URL img = getClass().getResource("/circuitimages/"+ nextRace.getImageName());
        String imgPath = URLDecoder.decode(img.getPath(), StandardCharsets.UTF_8);
        File file = new File(imgPath);

        channelList.forEach(x ->
            x.sendMessageEmbeds(EmbedCreator.createUpcoming(nextRace).build())
                    .addFiles(FileUpload.fromData(file, "circuitImage.png"))
                    .queue()
        );
    }
}
