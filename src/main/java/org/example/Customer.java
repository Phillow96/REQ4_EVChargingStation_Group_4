package org.example;

public class Customer {

    private String name;
    private final PrepaidAccount prepaidAccount;

    public Customer(String name){
        this.name = name;
        this.prepaidAccount = new PrepaidAccount();
    }

    public PrepaidAccount getPrepaidAccount(){
        return this.prepaidAccount;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
