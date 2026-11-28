
package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Epic2StepDefinitions {

    // Managers
    private ChargingLocationManager manager;
    private ChargingPointManager pointManager;

    // Selected objects
    private ChargingLocation selectedLocation;

    // Test results
    private List<ChargingLocation> result;
    private List<ChargingPoint> chargingPointResult;
    private String chargingStateResult;
    private String errorMessage;


    // Background

    @Given("an EV Charging Network")
    public void createNetwork() {
        EVChargingNetwork network = new EVChargingNetwork("Vienna EV Power");

        manager = network.getChargingLocationManager();
        pointManager = network.getChargingPointManager();
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
        chargingPointResult = pointManager.readChargingPoints(location);
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

    // Error Cases

    @Then("an error message {string} is returned")
    public void checkErrorMessage(String expectedMessage) {

        assertThat(errorMessage).isEqualTo(expectedMessage);
    }
}
