package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    protected ArrayList<Customer> customers;
    private int registeredCustomers;

    public CustomerManager(){
        this.customers = new ArrayList<Customer>();
        this.registeredCustomers = 0;
    }

    public int getRegisteredCustomers() {
        return this.registeredCustomers;
    }

    public void registerCustomer(String name) {
        this.customers.add(new Customer(name));
        this.registeredCustomers += 1;
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
        return null;
    }

}
