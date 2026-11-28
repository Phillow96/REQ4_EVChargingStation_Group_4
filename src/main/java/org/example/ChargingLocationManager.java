package org.example;

import java.util.List;
import java.util.ArrayList;


public class ChargingLocationManager {

    private ArrayList<ChargingLocation> chargingLocations;

    public ChargingLocationManager() {
        this.chargingLocations = new ArrayList<>();
    }

    public ChargingLocationManager createChargingLocation(String name, String address) {
        this.chargingLocations.add(new ChargingLocation(name, address));
        return this;
    }

    public List<ChargingLocation> readChargingLocations() {
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

    @Override
    public String toString() {
        return this.chargingLocations.toString();
    }
}
