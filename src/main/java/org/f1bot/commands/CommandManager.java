package org.f1bot.commands;

import org.f1bot.commands.f1commands.ConstructorStandings;
import org.f1bot.commands.f1commands.DriverStandings;
import org.f1bot.commands.f1commands.GetRace;
import org.f1bot.commands.f1commands.NextRace;
import org.f1bot.f1data.F1Data;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private HashMap<String, BotCommand> commandMap = new HashMap<>();
    public CommandManager(F1Data f1Data) {
        BotCommand ping = new Ping(
                "ping",
                "ping the bot :)");
        commandMap.put(ping.name, ping);
        BotCommand getRace = new GetRace(
                "getrace",
                "Get info from a specific Grand Prix", f1Data);
        commandMap.put(getRace.name,getRace);
        BotCommand nextRace = new NextRace(
                "nextrace",
                "Get info from the next Grand Prix");
        commandMap.put(nextRace.name, nextRace);
        BotCommand driverStandings = new DriverStandings(
                "driverstandings",
                "Get the current standings in the drivers championship");
        commandMap.put(driverStandings.name, driverStandings);
        BotCommand constructorStandings = new ConstructorStandings(
                "constructorstandings",
                "Get the current standings in the constructor championship");
        commandMap.put(constructorStandings.name, constructorStandings);
    }
    public HashMap<String, BotCommand> getCommands() { return commandMap; }
    public List<BotCommand> getCommandList() {
        return commandMap.values().stream().toList();
    }
}
