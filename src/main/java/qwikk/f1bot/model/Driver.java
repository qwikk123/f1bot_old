package qwikk.f1bot.model;

public record Driver (int pos,
                      String name,
                      String constructorName,
                      double points,
                      int wins,
                      String code,
                      String nationality,
                      String driverId,
                      int permanentNumber) {}
