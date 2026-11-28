
package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class Epic2StepDefinitions {

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


    // Background

    @Given("an EV Charging Network")
    public void createNetwork() {
        EVChargingNetwork network = new EVChargingNetwork("Vienna EV Power");

        manager = network.getChargingLocationManager();
        pointManager = network.getChargingPointManager();
        pricingRatesManager = network.getPricingRatesManager();
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

    // Error Cases

    @Then("an error message {string} is returned")
    public void checkErrorMessage(String expectedMessage) {

        assertThat(errorMessage).isEqualTo(expectedMessage);
    }
}
