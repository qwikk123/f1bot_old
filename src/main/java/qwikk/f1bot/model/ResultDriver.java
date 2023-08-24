package qwikk.f1bot.model;

public record ResultDriver (
        String driverId,
        int laps,
        int gridStart,
        String status,
        double points
) {}
