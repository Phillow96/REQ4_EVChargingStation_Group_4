package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    protected ArrayList<Customer> customers;

    public CustomerManager(){
        this.customers = new ArrayList<Customer>();
    }

    public void registerCustomer(String name) {
        this.customers.add(new Customer(name));
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
