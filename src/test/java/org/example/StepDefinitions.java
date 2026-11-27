
package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private EVChargingNetwork network;
    private double returnedBalance;

    // New variables for Owner requirements
    private OwnerAccountSteps ownerAccountSteps;
    private List<Customer> returnedAccounts;

    private CustomerManager customers() {
        return network.getCustomerManager();
    }

    @Given("the EV Charging Network {string} is in operation")
    public void theEvChargingNetworkIsInOperation(String networkName) {
        network = new EVChargingNetwork(networkName);

        // Initialize Owner functionality
        ownerAccountSteps =
                new OwnerAccountSteps(network.getCustomerManager());
    }

    // ==========================================
    // EXISTING CUSTOMER STEP DEFINITIONS
    // ==========================================

    @When("the Customer {string} completes the Registration")
    public void theCustomerCompletesTheRegistration(String name) {
        customers().registerCustomer(name);
    }

    @Then("the EV Charging Network holds the Customer {string}")
    public void theNetworkHoldsTheCustomer(String name) {
        assertThat(customers().holdsCustomer(name)).isTrue();
    }

    @Then("the Account Balance of the Customer {string} is {double} EUR")
    public void theAccountBalanceOfTheCustomerIs(String name, Double expected) {
        assertThat(customers()
                .readCustomer(name)
                .getPrepaidAccount()
                .getBalance()).isEqualTo(expected);
    }

    @Given("the Customer {string} is registered in the EV Charging Network")
    public void theCustomerIsRegisteredInTheNetwork(String name) {
        customers().registerCustomer(name);
    }

    @When("the Customer {string} tops up {double} EUR")
    public void theCustomerTopsUp(String name, Double amount) {
        customers()
                .readCustomer(name)
                .getPrepaidAccount()
                .topUp(amount);
    }

    @When("the following Customers complete the Registration:")
    public void theFollowingCustomersCompleteTheRegistration(DataTable table) {
        List<String> names = table.asList(String.class);

        for (String name : names) {
            customers().registerCustomer(name);
        }
    }

    @Then("the number of registered Customers is {int}")
    public void theNumberOfRegisteredCustomersIs(int expected) {
        assertThat(customers().getRegisteredCustomers())
                .isEqualTo(expected);
    }

    @When("the Customer {string} requests the Account Balance")
    public void theCustomerRequestsTheAccountBalance(String name) {
        returnedBalance = customers()
                .readCustomer(name)
                .getPrepaidAccount()
                .getBalance();
    }

    @Then("an Account Balance of {double} EUR is returned")
    public void anAccountBalanceOfEURIsReturned(double expected) {
        assertEquals(expected, returnedBalance, 0.001);
    }

    // ==========================================
    // NEW: EP-8 OWNER CUSTOMER ACCOUNT MANAGEMENT
    // ==========================================

    // US-8.1: Access registered prepaid accounts

    @When("the Owner requests all registered Prepaid Accounts")
    public void ownerRequestsAllRegisteredPrepaidAccounts() {
        returnedAccounts = ownerAccountSteps.getRegisteredAccounts();
    }

    @Then("the Owner receives {int} Prepaid Accounts")
    public void ownerReceivesPrepaidAccounts(int expected) {
        assertThat(returnedAccounts).hasSize(expected);
    }

    @Then("the Owner can access the Prepaid Account of {string}")
    public void ownerCanAccessPrepaidAccount(String name) {
        assertThat(returnedAccounts)
                .anyMatch(customer ->
                        customer.getName().equals(name)
                                && customer.getPrepaidAccount() != null);
    }

    // US-8.2: See each customer's current account balance

    @When("the Owner requests the Account Balance of {string}")
    public void ownerRequestsAccountBalance(String name) {
        returnedBalance = ownerAccountSteps.getCustomerBalance(name);
    }

    @Then("the Owner receives an Account Balance of {double} EUR")
    public void ownerReceivesAccountBalance(double expected) {
        assertEquals(expected, returnedBalance, 0.001);
    }
}
