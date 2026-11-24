package org.example;

public class EVChargingNetwork {

    private String name;
    CustomerManager customerManager;

    public EVChargingNetwork (String name){
        this.name = name;
        this.customerManager = new CustomerManager();
    }

    public CustomerManager getCustomerManager(){
        return this.customerManager;
    }
}
