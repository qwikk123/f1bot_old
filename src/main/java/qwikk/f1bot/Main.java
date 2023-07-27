package qwikk.f1bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import qwikk.f1bot.commands.CommandListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File f = new File("token/t.token");
        try (Scanner s = new Scanner(f)) {
            JDA bot = JDABuilder.createDefault(s.nextLine())
                    .setActivity(Activity.listening("F1 theme song"))
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build();
            bot.awaitReady();
            CommandListener commandListener = new CommandListener(bot);
            commandListener.upsertCommands(bot.getGuilds());
            bot.addEventListener(commandListener);
        }
        catch (FileNotFoundException e) {
            System.out.println("Token file is missing");
        } catch (InterruptedException e) {
            System.out.println("Bot setup failed");
        }
    }
}