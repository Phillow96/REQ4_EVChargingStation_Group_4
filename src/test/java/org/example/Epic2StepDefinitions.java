package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Epic2StepDefinitions {

    private ChargingLocationManager manager;
    private List<ChargingLocation> result;
    private ChargingLocation selectedLocation;
    private List<ChargingPoint> chargingPointResult;

    @Given("an EV Charging Network")
    public void createNetwork() {
        EVChargingNetwork network = new EVChargingNetwork("Vienna EV Power");
        manager = network.getChargingLocationManager();
    }

    @Given("the following Charging Locations exist:")
    public void createChargingLocations(DataTable table) {

        for (Map<String, String> row : table.asMaps()) {
            manager.createChargingLocation(
                    row.get("Charging Location"));
        }
    }

    @Given("the Charging Location {string} exists")
    public void createChargingLocation(String name) {
        manager.createChargingLocation(name);
        selectedLocation = manager.readChargingLocation(name);
    }

    @Given("the Charging Location has the following Charging Points:")
    public void addChargingPoints(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            selectedLocation.addChargingPoint(
                    row.get("Charging Point"),
                    row.get("Charging Mode"));
        }
    }

    @When("the Customer requests the Charging Points of {string}")
    public void requestChargingPoints(String name) {
        ChargingLocation location = manager.readChargingLocation(name);
        chargingPointResult = location.getChargingPoints();
    }

    @Then("{int} Charging Points are returned")
    public void checkChargingPoints(int expectedNumber) {
        assertThat(chargingPointResult.size()).isEqualTo(expectedNumber);
    }

    @When("the Customer requests all Charging Locations")
    public void requestChargingLocations() {
        result = manager.getChargingLocations();
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

    @Then("the Charging Point {string} with Charging Mode {string} is returned")
    public void checkChargingPoint(String id, String chargingMode) {

        boolean found = false;

        for (ChargingPoint point : chargingPointResult) {
            if (point.getId().equals(id)
                    && point.getChargingMode().equals(chargingMode)) {
                found = true;
            }
        }

        assertThat(found).isTrue();
    }

        @Then("{int} Charging Locations are returned")
    public void checkChargingLocations(int expectedNumber) {
        assertThat(result.size()).isEqualTo(expectedNumber);
    }
}