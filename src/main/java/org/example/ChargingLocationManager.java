package org.example;

import java.util.ArrayList;

public class ChargingLocationManager {

    private ArrayList<ChargingLocation> chargingLocations;

    public ChargingLocationManager() {
        this.chargingLocations = new ArrayList<>();
    }

    public void createChargingLocation(String name) {
        this.chargingLocations.add(new ChargingLocation(name));
    }

    public ArrayList<ChargingLocation> getChargingLocations() {
        return this.chargingLocations;
    }

    public int getNumberOfChargingLocations() {
        return this.chargingLocations.size();
    }

    public ChargingLocation readChargingLocation(String name) {
        for (ChargingLocation location : chargingLocations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }
}
