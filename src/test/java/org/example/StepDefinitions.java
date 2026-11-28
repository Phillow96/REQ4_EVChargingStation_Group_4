package org.example;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StepDefinitions {

    private EVChargingNetwork network;
    private double returnedBalance;

    // Managers
    private ChargingLocationManager manager;
    private ChargingPointManager pointManager;
    private PricingRatesManager pricingRatesManager;

    // Selected objects
    private ChargingLocation selectedLocation;

    // Test results
    private List<ChargingLocation> result;
    private List<ChargingPoint> chargingPointResult;
    private String chargingStateResult;
    private String errorMessage;
    private PricingRates pricingRatesResult;
    private ChargingLocationDetails locationDetailsResult;

    private CustomerManager customers() {
        return network.getCustomerManager();
    }

    @Given("the EV Charging Network {string} is in operation")
    public void theEvChargingNetworkIsInOperation(String networkName) {
        network = new EVChargingNetwork(networkName);
        manager = network.getChargingLocationManager();
        pointManager = network.getChargingPointManager();
        pricingRatesManager = network.getPricingRatesManager();
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
        assertThat(customers().readCustomer(name).getPrepaidAccount().getBalance()).isEqualTo(expected);
    }

    @Given("the Customer {string} is registered in the EV Charging Network")
    public void theCustomerIsRegisteredInTheNetwork(String name) {
        customers().registerCustomer(name);
    }

    @When("the Customer {string} tops up {double} EUR")
    public void theCustomerTopsUp(String name, Double amount) {
        customers().readCustomer(name).getPrepaidAccount().topUp(amount);
    }

    @When("the following Customers complete the Registration:")
    public void theFollowingCustomersCompleteTheRegistration(DataTable table) {
        List<String> names = table.asList(String.class);

        for (String name : names) {
            customers().registerCustomer(name);
        }
    }

    @Then("the number of registered Customers is {int}")
    public void theNumberOfRegisteredCustomersIs(int expected){
        assertThat(customers().getRegisteredCustomers()).isEqualTo(expected);
    }

    @When("the Customer {string} requests the Account Balance")
    public void theCustomerRequestsTheAccountBalance(String name) {
        returnedBalance = customers().readCustomer(name).getPrepaidAccount().getBalance();
    }

    @Then("an Account Balance of {double} EUR is returned")
    public void anAccountBalanceOfEURIsReturned(double expected) {
        assertEquals(expected, returnedBalance, 0.001);
    }
    // Epic 2 - Find Charging Location and Check Charging State

    // Background

    @Given("an EV Charging Network")
    public void createNetwork() {
        theEvChargingNetworkIsInOperation("Vienna EV Power");
    }

    // US-2.1 - View all Charging Locations

    @Given("the following Charging Locations exist:")
    public void createChargingLocations(DataTable table) {

        for (Map<String, String> row : table.asMaps()) {
            manager.createChargingLocation(
                    row.get("Charging Location"),
                    row.get("Address"));
        }
    }

    @When("the Customer requests all Charging Locations")
    public void requestChargingLocations() {
        result = manager.readChargingLocations();
    }

    @Then("{int} Charging Locations are returned")
    public void checkChargingLocations(int expectedNumber) {
        assertThat(result.size()).isEqualTo(expectedNumber);
    }

    @Then("the Charging Location {string} is returned")
    public void checkChargingLocation(String name) {

        boolean found = false;

        for (ChargingLocation location : result) {
            if (location.getName().equals(name)) {
                found = true;
            }
        }

        assertThat(found).isTrue();
    }

    // US-2.1 - View Charging Points of a Charging Location

    @Given("the Charging Location {string} exists")
    public void createChargingLocation(String name) {
        manager.createChargingLocation(name, "Test Street 1");
        selectedLocation = manager.readChargingLocation(name);
    }

    @Given("the Charging Location has the following Charging Points:")
    public void addChargingPoints(DataTable table) {

        for (Map<String, String> row : table.asMaps()) {
            pointManager.createChargingPoint(
                    row.get("Charging Point"),
                    row.get("Charging Mode"),
                    selectedLocation);
        }
    }

    @When("the Customer requests the Charging Points of {string}")
    public void requestChargingPoints(String name) {

        ChargingLocation location = manager.readChargingLocation(name);

        try {
            chargingPointResult = pointManager.readChargingPoints(location);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("{int} Charging Points are returned")
    public void checkChargingPoints(int expectedNumber) {
        assertThat(chargingPointResult.size()).isEqualTo(expectedNumber);
    }

    @Then("the Charging Point {string} with Charging Mode {string} is returned")
    public void checkChargingPoint(String name, String chargingMode) {

        boolean found = false;

        for (ChargingPoint point : chargingPointResult) {
            if (point.getName().equals(name)
                    && point.getChargingMode().equals(chargingMode)) {
                found = true;
            }
        }

        assertThat(found).isTrue();
    }

    // US-2.2 - View the Charging State of a Charging Point

    @Given("the Charging Point {string} has the Charging State {string}")
    public void createChargingPointWithState(String name, String state) {

        manager.createChargingLocation("Vienna Central", "Test Street 1");

        ChargingLocation location =
                manager.readChargingLocation("Vienna Central");

        pointManager.createChargingPoint(name, "AC", location);

        pointManager.updateChargingState(name, state);
    }

    @When("the Customer requests the Charging State of {string}")
    public void requestChargingState(String name) {

        try {
            chargingStateResult = pointManager.readChargingState(name);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the Charging State is {string}")
    public void checkChargingState(String expectedState) {

        assertThat(chargingStateResult).isEqualTo(expectedState);
    }

    // US-2.3 - View Current Pricing Rates

    @Given("the Charging Location has the following Pricing Rates:")
    public void createPricingRates(DataTable table) {

        Map<String, String> ac = null;
        Map<String, String> dc = null;

        for (Map<String, String> row : table.asMaps()) {

            if (row.get("Charging Mode").equals("AC")) {
                ac = row;
            } else if (row.get("Charging Mode").equals("DC")) {
                dc = row;
            }
        }

        assertThat(ac).isNotNull();
        assertThat(dc).isNotNull();

        PricingRates rates = new PricingRates(
                "PR-" + selectedLocation.getName(),
                LocalDateTime.now().minusMinutes(1),
                Double.parseDouble(ac.get("Energy Rate")),
                Double.parseDouble(dc.get("Energy Rate")),
                Double.parseDouble(ac.get("Parking Rate")),
                Double.parseDouble(dc.get("Parking Rate"))
        );

        pricingRatesManager.createPricingRates(selectedLocation, rates);
    }

    @When("the Customer requests the Pricing Rates of {string}")
    public void requestPricingRates(String name) {

        ChargingLocation location = manager.readChargingLocation(name);

        try {
            pricingRatesResult =
                    pricingRatesManager.getCurrentPricingRates(location);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the following Pricing Rates are returned:")
    public void checkPricingRates(DataTable table) {

        assertThat(pricingRatesResult).isNotNull();

        for (Map<String, String> row : table.asMaps()) {

            String mode = row.get("Charging Mode");

            double energyRate =
                    Double.parseDouble(row.get("Energy Rate"));

            double parkingRate =
                    Double.parseDouble(row.get("Parking Rate"));

            if (mode.equals("AC")) {

                assertThat(pricingRatesResult.getAcEnergyRate())
                        .isEqualTo(energyRate);

                assertThat(pricingRatesResult.getAcParkingRate())
                        .isEqualTo(parkingRate);

            } else if (mode.equals("DC")) {

                assertThat(pricingRatesResult.getDcEnergyRate())
                        .isEqualTo(energyRate);

                assertThat(pricingRatesResult.getDcParkingRate())
                        .isEqualTo(parkingRate);

            } else {
                throw new IllegalArgumentException("Unknown Charging Mode");
            }
        }
    }

    @Given("the Charging Location has the following scheduled Pricing Rates:")
    public void createScheduledPricingRates(DataTable table) {

        LocalDateTime now = LocalDateTime.now();

        for (Map<String, String> row : table.asMaps()) {

            PricingRates rates = new PricingRates(
                    row.get("Pricing Rates ID"),
                    now.plusMinutes(
                            Integer.parseInt(row.get("Valid From Offset"))),
                    Double.parseDouble(row.get("AC Energy Rate")),
                    Double.parseDouble(row.get("DC Energy Rate")),
                    Double.parseDouble(row.get("AC Parking Rate")),
                    Double.parseDouble(row.get("DC Parking Rate"))
            );

            pricingRatesManager.createPricingRates(selectedLocation, rates);
        }
    }

    // US-2.6 - View Charging Location Details
    @Given("the Charging Location {string} with Address {string} exists")
    public void createChargingLocationWithAddress(String name, String address) {

        manager.createChargingLocation(name, address);

        selectedLocation = manager.readChargingLocation(name);
    }

    @When("the Customer requests the details of {string}")
    public void requestLocationDetails(String name) {

        try {
            locationDetailsResult =
                    network.readChargingLocationDetails(name);

            chargingPointResult =
                    locationDetailsResult.getChargingPoints();

            pricingRatesResult =
                    locationDetailsResult.getPricingRates();

        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the Address is {string}")
    public void checkAddress(String expectedAddress) {

        assertThat(locationDetailsResult).isNotNull();

        assertThat(locationDetailsResult.getLocation().getAddress())
                .isEqualTo(expectedAddress);
    }

    // Error Cases

    @Then("an error message {string} is returned")
    public void checkErrorMessage(String expectedMessage) {

        assertThat(errorMessage).isEqualTo(expectedMessage);
    }
}
