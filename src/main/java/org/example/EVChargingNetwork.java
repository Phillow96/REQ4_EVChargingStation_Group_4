package org.example;

import java.util.List;

public class EVChargingNetwork {

    private String networkName;
    private CustomerManager customerManager;
    private ChargingLocationManager chargingLocationManager;
    private ChargingPointManager chargingPointManager;
    private PricingRatesManager pricingRatesManager;

    public EVChargingNetwork (String name){
        this.networkName = name;
        this.customerManager = new CustomerManager();
        this.chargingLocationManager = new ChargingLocationManager();
        this.chargingPointManager =
                new ChargingPointManager(this.chargingLocationManager);
        this.pricingRatesManager = new PricingRatesManager();
    }

    public CustomerManager getCustomerManager() {
        return this.customerManager;
    }

    public ChargingLocationManager getChargingLocationManager() {
        return this.chargingLocationManager;
    }

    public ChargingPointManager getChargingPointManager() {
        return this.chargingPointManager;
    }

    public PricingRatesManager getPricingRatesManager() {
        return this.pricingRatesManager;
    }

    public ChargingLocationDetails readChargingLocationDetails(String name) {

        ChargingLocation location =
                chargingLocationManager.readChargingLocation(name);

        List<ChargingPoint> chargingPoints =
                chargingPointManager.readChargingPoints(location);

        PricingRates rates =
                pricingRatesManager.getCurrentPricingRates(location);

        return new ChargingLocationDetails(
                location, chargingPoints, rates);
    }

    public String getNetworkName() {
        return this.networkName;
    }

    @Override
    public String toString() {
        return this.networkName;
    }
}
