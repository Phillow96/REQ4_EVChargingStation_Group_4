
package org.example;

import java.util.List;

public class ChargingPointManager {

    private ChargingLocationManager locationManager;

    public ChargingPointManager(ChargingLocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public ChargingPointManager createChargingPoint(
            String name, String mode, ChargingLocation location) {

        location.addChargingPoint(name, mode);
        return this;
    }

    public ChargingPoint readChargingPoint(String name) {

        for (ChargingLocation location : locationManager.readChargingLocations()) {
            for (ChargingPoint point : location.getChargingPoints()) {
                if (point.getName().equals(name)) {
                    return point;
                }
            }
        }

        return null;
    }

    public String readChargingState(String name) {

        ChargingPoint point = readChargingPoint(name);

        if (point == null) {
            throw new IllegalArgumentException("Unknown Charging Point");
        }

        return point.getChargingState();
    }

    public ChargingPointManager updateChargingState(String name, String state) {

        ChargingPoint point = readChargingPoint(name);

        if (point != null) {
            point.setChargingState(state);
        }

        return this;
    }

    public List<ChargingPoint> readChargingPoints(ChargingLocation location) {

        if (location == null) {
            throw new IllegalArgumentException("Unknown Charging Location");
        }

        return location.getChargingPoints();
    }
}
