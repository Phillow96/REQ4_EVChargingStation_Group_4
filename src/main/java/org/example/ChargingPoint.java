
package org.example;

public class ChargingPoint {

    private String name;
    private String chargingMode;
    private String chargingState;

    public ChargingPoint(String name, String chargingMode) {
        this.name = name;
        this.chargingMode = chargingMode;
        this.chargingState = "available";
    }

    public void setChargingState(String chargingState) {
        this.chargingState = chargingState;
    }

    public String getName() {
        return this.name;
    }

    public String getChargingMode() {
        return this.chargingMode;
    }

    public String getChargingState() {
        return this.chargingState;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.chargingMode;
    }
}
