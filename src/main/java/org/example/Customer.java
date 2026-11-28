package org.example;

public class Customer {

    private String name;
    private final PrepaidAccount prepaidAccount;

    public Customer(String name){
        if (name == null || name.isBlank()) {
            throw new ChargingNetworkException("A Customer needs a name");
        }
        this.name = name;
        this.prepaidAccount = new PrepaidAccount();
    }

    public Customer withName(String name) {
        if (name == null || name.isBlank()) {
            throw new ChargingNetworkException("A Customer needs a name");
        }
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Customer [name=%s, %s]", name, prepaidAccount);
    }

    public PrepaidAccount getPrepaidAccount(){
        return this.prepaidAccount;
    }

    public String getName(){
        return this.name;
    }

}
