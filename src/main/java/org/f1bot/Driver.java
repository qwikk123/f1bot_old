package org.f1bot;

public class Driver {
    int pos;
    String name;
    String constructorName;
    double points;
    int wins;

    public Driver(int pos, String name, String constructorName, double points, int wins) {
        this.pos = pos;
        this.name = name;
        this.constructorName = constructorName;
        this.points = points;
        this.wins = wins;
    }
}
