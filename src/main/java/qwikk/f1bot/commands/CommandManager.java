package qwikk.f1bot.commands;

import qwikk.f1bot.commands.botcommands.*;
import qwikk.f1bot.service.F1DataService;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private final HashMap<String, BotCommand> commandMap = new HashMap<>();
    public CommandManager(F1DataService f1DataService) {
        BotCommand ping = new Ping(
                "ping",
                "ping the bot :)");
        commandMap.put(ping.getName(), ping);

        BotCommand getRace = new GetRace(
                "getrace",
                "Get info from a specific Grand Prix",
                f1DataService.getRaceList());
        commandMap.put(getRace.getName(),getRace);

        BotCommand nextRace = new NextRace(
                "nextrace",
                "Get info from the next Grand Prix",
                f1DataService.getRaceList());
        commandMap.put(nextRace.getName(), nextRace);

        BotCommand driverStandings = new DriverStandings(
                "driverstandings",
                "Get the current standings in the drivers championship",
                f1DataService.getDriverMap());
        commandMap.put(driverStandings.getName(), driverStandings);

        BotCommand constructorStandings = new ConstructorStandings(
                "constructorstandings",
                "Get the current standings in the constructor championship",
                f1DataService.getConstructorStandings());
        commandMap.put(constructorStandings.getName(), constructorStandings);

        BotCommand getDriver = new GetDriver(
                "getdriver",
                "get information about a driver",
                f1DataService.getDriverMap());
        commandMap.put(getDriver.getName(), getDriver);

    }
    public HashMap<String, BotCommand> getCommands() { return commandMap; }
    public List<BotCommand> getCommandList() {
        return commandMap.values().stream().toList();
    }
}
