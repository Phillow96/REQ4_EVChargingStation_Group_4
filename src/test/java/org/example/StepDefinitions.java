package org.example;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StepDefinitions {

    private EVChargingNetwork network;

    private CustomerManager customers() {
        return network.getCustomerManager();
    }

    @Given("the EV Charging Network {string} is in operation")
    public void theEvChargingNetworkIsInOperation(String networkName) {
        network = new EVChargingNetwork(networkName);
    }

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
        assertThat(customers().readCustomer(name).getBalance()).isEqualTo(expected);
    }

}
