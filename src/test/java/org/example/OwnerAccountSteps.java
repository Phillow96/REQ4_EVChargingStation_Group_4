
package org.example;

import java.util.List;

public class OwnerAccountSteps {

    private final CustomerManager customerManager;

    public OwnerAccountSteps(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    // US-8.1: Access registered prepaid accounts
    public List<Customer> getRegisteredAccounts() {
        return customerManager.getAllCustomers();
    }

    // US-8.2: See a customer's current account balance
    public double getCustomerBalance(String name) {
        Customer customer = customerManager.readCustomer(name);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found: " + name
            );
        }

        return customer.getPrepaidAccount().getBalance();
    }
}