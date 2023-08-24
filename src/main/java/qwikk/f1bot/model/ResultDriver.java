package qwikk.f1bot.model;

/**
 * Record for race result information tied to a driver.
 * @param driverId
 * @param laps
 * @param gridStart
 * @param status
 * @param points
 */
public record ResultDriver (
        String driverId,
        int laps,
        int gridStart,
        String status,
        double points
) {}
