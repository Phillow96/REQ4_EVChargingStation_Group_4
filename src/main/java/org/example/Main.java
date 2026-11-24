package org.example;

public class Main {
    public static void main(String[] args) {
        EVChargingNetwork network = new EVChargingNetwork("Vienna");
        CustomerManager cM = network.getCustomerManager();

        cM.registerCustomer("Anna Berger");
        System.out.println(cM.holdsCustomer("Anna Berger"));

    }
}
