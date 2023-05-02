package org.f1bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.f1bot.f1data.F1Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        File f = new File("token/t.token");
        Scanner s = new Scanner(f);

        JDA bot = JDABuilder.createDefault(s.nextLine())
                .setActivity(Activity.listening("F1 theme song"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        bot.addEventListener(new CommandManager(new F1Data(bot)));
        bot.awaitReady();

        s.close();
    }
}