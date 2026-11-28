
package org.example;

public class ChargingPoint {

    private String id;
    private String chargingMode;

    public ChargingPoint(String id, String chargingMode) {
        this.id = id;
        this.chargingMode = chargingMode;
    }

    public String getId() {
        return this.id;
    }

    public String getChargingMode() {
        return this.chargingMode;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.chargingMode;
    }
}
