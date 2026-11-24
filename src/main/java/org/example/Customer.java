package org.example;

public class Customer {

    private String name;
    private Double balance;

    public Customer(String name){
        this.name = name;
        this.balance = 0.0;
    }

    public double getBalance(){
        return this.balance;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
