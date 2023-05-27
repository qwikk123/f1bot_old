package qwikk.f1bot.commands;

import qwikk.f1bot.commands.f1commands.*;
import qwikk.f1bot.f1data.F1Data;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private final HashMap<String, BotCommand> commandMap = new HashMap<>();
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

        BotCommand getDriver = new GetDriver(
                "getdriver",
                "get information about a driver",
                f1Data);
        commandMap.put(getDriver.name, getDriver);

    }
    public HashMap<String, BotCommand> getCommands() { return commandMap; }
    public List<BotCommand> getCommandList() {
        return commandMap.values().stream().toList();
    }
}
