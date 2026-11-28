package org.example;

public class EVChargingNetwork {

    private String networkName;
    private CustomerManager customerManager;
    private ChargingLocationManager chargingLocationManager;
    private ChargingPointManager chargingPointManager;

    public EVChargingNetwork (String name){
        this.networkName = name;
        this.customerManager = new CustomerManager();
        this.chargingLocationManager = new ChargingLocationManager();
        this.chargingPointManager =
                new ChargingPointManager(this.chargingLocationManager);
    }

    public ChargingPointManager getChargingPointManager() {
        return this.chargingPointManager;
    }

    public CustomerManager getCustomerManager(){
        return this.customerManager;
    }

    public ChargingLocationManager getChargingLocationManager() {
        return this.chargingLocationManager;
    }

    public String getNetworkName() {
        return this.networkName;
    }
}
