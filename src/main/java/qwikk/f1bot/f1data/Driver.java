package qwikk.f1bot.f1data;

public record Driver (int pos,
                      String name,
                      String constructorName,
                      double points,
                      int wins,
                      String code,
                      String nationality,
                      String driverId,
                      int permanentNumber) {}
