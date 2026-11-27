package org.example;

public class EVChargingNetwork {

    private String networkName;
    private CustomerManager customerManager;

    public EVChargingNetwork (String name){
        this.networkName = name;
        this.customerManager = new CustomerManager();
    }

    public CustomerManager getCustomerManager(){
        return this.customerManager;
    }

    public String getNetworkName() {
        return this.networkName;
    }
}
