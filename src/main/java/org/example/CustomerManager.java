package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    protected ArrayList<Customer> customers;

    public CustomerManager(){
        this.customers = new ArrayList<Customer>();
    }

    public int getRegisteredCustomers() {
        return this.customers.size();
    }

    public CustomerManager registerCustomer(String name) {
        Customer customer = new Customer(name);
        if (holdsCustomer((customer.getName()))) {
            throw new ChargingNetworkException("Customer is already registered");
        }
        this.customers.add(customer);
        return this;
    }

    public boolean holdsCustomer(String name) {
        for(Customer c : customers){
            if (c.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Customer readCustomer(String name) {
        for(Customer c : customers){
            if (c.getName().equals(name)){
                return c;
            }
        }
        throw new ChargingNetworkException("Unknown Customer");
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("CustomerManager [" + customers.size() + "Customers]");
        for (Customer c : customers) {
            text.append("\n ").append(c);
        }
        return text.toString();
    }

}
