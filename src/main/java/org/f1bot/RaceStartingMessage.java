package org.f1bot;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;

public class RaceStartingMessage implements Runnable{

    TextChannel channel;
    LocalDateTime scheduledTime;
    Race nextRace;
    public RaceStartingMessage (TextChannel channel, LocalDateTime scheduledTime, Race nextRace) {
        this.channel = channel;
        this.scheduledTime = scheduledTime;
        this.nextRace = nextRace;
    }
    @Override
    public void run() {
        System.out.println("Scheduled at: "+scheduledTime+" Running at: "+LocalDateTime.now());
        channel.sendMessageEmbeds(EmbedCreator.createUpcoming(nextRace).build()).queue();
    }
}
