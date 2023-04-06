package org.f1bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        F1Data f1Data = new F1Data();
        f1Data.setF1RaceData();
        f1Data.setNextRace();
        f1Data.setF1DriverStandingsData();
        f1Data.setF1ConstructorStandingsData();

        File f = new File("token/t.token");
        Scanner s = new Scanner(f);

        JDA bot = JDABuilder.createDefault(s.nextLine())
                .setActivity(Activity.listening("F1 theme song"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        bot.addEventListener(new CommandManager(f1Data));
        s.close();
    }
}