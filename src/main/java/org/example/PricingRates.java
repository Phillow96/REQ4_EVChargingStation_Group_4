
package org.example;

import java.time.LocalDateTime;

public class PricingRates {

    private final String pricingRatesId;
    private final LocalDateTime validFrom;
    private final double acEnergyRate;
    private final double dcEnergyRate;
    private final double acParkingRate;
    private final double dcParkingRate;

    public PricingRates(String pricingRatesId,
                        LocalDateTime validFrom,
                        double acEnergyRate,
                        double dcEnergyRate,
                        double acParkingRate,
                        double dcParkingRate) {

        this.pricingRatesId = pricingRatesId;
        this.validFrom = validFrom;
        this.acEnergyRate = acEnergyRate;
        this.dcEnergyRate = dcEnergyRate;
        this.acParkingRate = acParkingRate;
        this.dcParkingRate = dcParkingRate;
    }

    public String getPricingRatesId() {
        return this.pricingRatesId;
    }

    public LocalDateTime getValidFrom() {
        return this.validFrom;
    }

    public double getAcEnergyRate() {
        return this.acEnergyRate;
    }

    public double getDcEnergyRate() {
        return this.dcEnergyRate;
    }

    public double getAcParkingRate() {
        return this.acParkingRate;
    }

    public double getDcParkingRate() {
        return this.dcParkingRate;
    }

    @Override
    public String toString() {
        return "AC Energy Rate: " + acEnergyRate + " EUR/kWh"
                + "\nAC Parking Rate: " + acParkingRate + " EUR/min"
                + "\nDC Energy Rate: " + dcEnergyRate + " EUR/kWh"
                + "\nDC Parking Rate: " + dcParkingRate + " EUR/min";
    }
}
