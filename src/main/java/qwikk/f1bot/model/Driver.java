package qwikk.f1bot.model;

/**
 * Record representing an F1 driver
 * @param pos
 * @param name
 * @param constructorName
 * @param points
 * @param wins
 * @param code
 * @param nationality
 * @param driverId
 * @param permanentNumber
 */
public record Driver (int pos,
                      String name,
                      String constructorName,
                      double points,
                      int wins,
                      String code,
                      String nationality,
                      String driverId,
                      int permanentNumber,
                      String isoCode) {}
