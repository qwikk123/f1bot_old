package qwikk.f1bot.model;

/**
 * Record representing an F1 constructor team.
 * @param pos
 * @param name
 * @param nationality
 * @param points
 * @param wins
 */
public record Constructor(int pos,
                          String name,
                          String nationality,
                          double points,
                          int wins) {}
