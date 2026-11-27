package org.example;

public class PrepaidAccount {
    private Double balance;

    public PrepaidAccount(){
        this.balance = 0.0;
    }

    public void topUp(Double amount) {
        this.balance += amount;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }

}
