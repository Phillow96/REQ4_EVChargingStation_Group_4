package org.example;

public class EVChargingNetwork {

    private String networkName;
    private CustomerManager customerManager;
    private ChargingLocationManager chargingLocationManager;

    public EVChargingNetwork (String name){
        this.networkName = name;
        this.customerManager = new CustomerManager();
        this.chargingLocationManager = new ChargingLocationManager();
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
