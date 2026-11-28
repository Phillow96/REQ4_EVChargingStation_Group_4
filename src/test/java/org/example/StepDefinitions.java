package org.example;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private EVChargingNetwork network;
    private double returnedBalance;
    private Customer returnedCustomer;
    private String refusalMessage;

    private CustomerManager customers() {
        return network.getCustomerManager();
    }

    private void capture(Runnable request) {
        refusalMessage = null;
        try {
            request.run();
        } catch (ChargingNetworkException exception) {
            refusalMessage = exception.getMessage();
        }
    }


    // ===================== GIVEN =====================
    @Given("the EV Charging Network {string} is in operation")
    public void theEvChargingNetworkIsInOperation(String networkName) {
        network = new EVChargingNetwork(networkName);
        returnedCustomer = null;
        refusalMessage = null;
    }

    @Given("the Customer {string} is registered in the EV Charging Network")
    public void theCustomerIsRegisteredInTheNetwork(String name) {
        capture(() -> customers().registerCustomer(name));
    }

    // ===================== WHEN =====================
    @When("the Customer {string} requests the Account Balance")
    public void theCustomerRequestsTheAccountBalance(String name) {
        capture(() -> returnedBalance =
                customers().readCustomer(name).getPrepaidAccount().getBalance());
    }

    @When("the Customer {string} completes the Registration")
    public void theCustomerCompletesTheRegistration(DataTable table) {
        capture(() -> {
            for (String name : table.asList(String.class)) {
                customers().registerCustomer(name);
            }
        });
    }

    @When("the Customer {string} tops up {double} EUR")
    public void theCustomerTopsUp(String name, Double amount) {
        capture(() -> customers().readCustomer(name).getPrepaidAccount().topUp(amount));
    }

    @When("the following Customers complete the Registration:")
    public void theFollowingCustomersCompleteTheRegistration(DataTable table) {
        capture(() -> {
            for (Map<String, String> row : table.asMaps()) {
                customers().registerCustomer(row.get("Customer"));
            }
        });
    }


    // ===================== THEN =====================
    @Then("the number of registered Customers is {int}")
    public void theNumberOfRegisteredCustomersIs(int expected){
        assertThat(customers().getRegisteredCustomers()).isEqualTo(expected);
    }

    @Then("the Account Balance of the Customer {string} is {double} EUR")
    public void theAccountBalanceOfTheCustomerIs(String name, Double expected) {
        assertThat(customers().readCustomer(name).getPrepaidAccount().getBalance()).isEqualTo(expected);
    }

    @Then("an Account Balance of {double} EUR is returned")
    public void anAccountBalanceOfEURIsReturned(double expected) {
        assertEquals(expected, returnedBalance, 0.001);
    }

    @Then("the request is refused with the message {string}")
    public void theRequestIsRefusedWithTheMessage(String expected) {
        assertThat(refusalMessage).isEqualTo(expected);
    }

    @Then("the EV Charging Network holds the Customer {string}")
    public void theNetworkHoldsTheCustomer(String name) {
        assertThat(customers().holdsCustomer(name)).isTrue();
    }

}
