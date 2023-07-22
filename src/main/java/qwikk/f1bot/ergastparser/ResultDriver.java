package qwikk.f1bot.ergastparser;

public record ResultDriver (
        String driverId,
        int laps,
        int gridStart,
        String status,
        double points
) {}
