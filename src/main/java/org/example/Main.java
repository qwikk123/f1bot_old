package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        F1Data f1Data = new F1Data();
        f1Data.setF1Data();
        f1Data.setNextRace();

        JDA bot = JDABuilder.createDefault("MTA5MTAyNzkzMzI5ODE4MDEzNw.GzkfqP.dMgWJar0oZi3xGNjZmxeJ3dvvk9szPzDh6Lf5Q")
                .setActivity(Activity.listening("F1 theme song"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        bot.addEventListener(new CommandManager(f1Data));
    }
}