package org.example;

public class PrepaidAccount {
    private Double balance;

    public PrepaidAccount(){
        this.balance = 0.0;
    }

    public PrepaidAccount topUp(Double amount) {
        if (amount == null || amount <= 5.0) {
            throw new ChargingNetworkException("A top-up needs to be at least 5.00 EUR");
        }
        this.balance += amount;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Prepaid Account [Account Balance=%.2f EUR]", balance);
    }

    public Double getBalance() {
        return this.balance;
    }

}
