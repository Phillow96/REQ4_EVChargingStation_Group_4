
package org.example;

import java.util.ArrayList;

public class ChargingLocation {

    private String name;
    private String address;
    private ArrayList<ChargingPoint> chargingPoints;

    public ChargingLocation(String name, String address) {
        this.name = name;
        this.address = address;
        this.chargingPoints = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public void addChargingPoint(String id, String chargingMode) {
        this.chargingPoints.add(new ChargingPoint(id, chargingMode));
    }

    public ArrayList<ChargingPoint> getChargingPoints() {
        return this.chargingPoints;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.address;
    }
}
