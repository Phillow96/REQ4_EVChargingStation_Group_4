package org.example;

import java.util.List;

public class ChargingLocationDetails {

    private final ChargingLocation location;
    private final List<ChargingPoint> chargingPoints;
    private final PricingRates pricingRates;

    public ChargingLocationDetails(
            ChargingLocation location,
            List<ChargingPoint> chargingPoints,
            PricingRates pricingRates) {

        this.location = location;
        this.chargingPoints = chargingPoints;
        this.pricingRates = pricingRates;
    }

    public ChargingLocation getLocation() {
        return this.location;
    }

    public List<ChargingPoint> getChargingPoints() {
        return this.chargingPoints;
    }

    public PricingRates getPricingRates() {
        return this.pricingRates;
    }
}
