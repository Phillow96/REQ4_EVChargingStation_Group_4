
@Customer @Epic2
Feature: Find Charging Location and Check Charging State

  As a Customer
  I want to find Charging Locations and check their Charging Points
  so that I can find a suitable Charging Point for my vehicle.

  Background:
    Given an EV Charging Network

  # US-2.1 - View Charging Locations and Charging Points

  @US2.1 @MVP1
  Scenario: View all Charging Locations
    Given the following Charging Locations exist:
      | Charging Location  |
      | Vienna Central     |
      | Vienna Floridsdorf |
    When the Customer requests all Charging Locations
    Then 2 Charging Locations are returned

  @US2.1 @MVP1
  Scenario: View Charging Points of a Charging Location
    Given the Charging Location "Vienna Central" exists
    And the Charging Location has the following Charging Points:
      | Charging Point | Charging Mode |
      | CP-01          | AC            |
      | CP-02          | DC            |
    When the Customer requests the Charging Points of "Vienna Central"
    Then 2 Charging Points are returned

  # US-2.2 - View the Charging State

  @US2.2 @MVP1
  Scenario: View an available Charging Point
    Given the Charging Point "CP-01" has the Charging State "available"
    When the Customer requests the Charging State of "CP-01"
    Then the Charging State is "available"

  @US2.2 @MVP1
  Scenario: View an occupied Charging Point
    Given the Charging Point "CP-02" has the Charging State "occupied"
    When the Customer requests the Charging State of "CP-02"
    Then the Charging State is "occupied"

  @US2.2 @MVP1
  Scenario: View a Charging Point that is out of order
    Given the Charging Point "CP-03" has the Charging State "out of order"
    When the Customer requests the Charging State of "CP-03"
    Then the Charging State is "out of order"

  # US-2.3 - View Pricing Rates

  @US2.3 @MVP2
  Scenario: View Pricing Rates of a Charging Location
    Given the Charging Location "Vienna Central" exists
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Time Rate |
      | AC            | 0.35        | 0.02      |
      | DC            | 0.55        | 0.05      |
    When the Customer requests the Pricing Rates of "Vienna Central"
    Then the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Time Rate |
      | AC            | 0.35        | 0.02      |
      | DC            | 0.55        | 0.05      |

  @US2.3 @MVP2
  Scenario: Pricing Rates differ between Charging Locations
    Given the Charging Location "Vienna Central" has an AC Energy Rate of 0.35 EUR
    And the Charging Location "Vienna Floridsdorf" has an AC Energy Rate of 0.40 EUR
    When the Customer requests the AC Energy Rates
    Then the Energy Rate of "Vienna Central" is 0.35 EUR
    And the Energy Rate of "Vienna Floridsdorf" is 0.40 EUR

  # US-2.4 - Filter Charging Points

  @US2.4 @MVP2
  Scenario: Filter Charging Points by Charging Mode
    Given the following Charging Points exist:
      | Charging Point | Charging Mode |
      | CP-01          | AC            |
      | CP-02          | DC            |
    When the Customer filters Charging Points by "AC"
    Then only the Charging Point "CP-01" is returned

  @US2.4 @MVP2
  Scenario: Filter Charging Points by Charging State
    Given the following Charging Points exist:
      | Charging Point | Charging State |
      | CP-01          | available      |
      | CP-02          | occupied       |
    When the Customer filters Charging Points by "available"
    Then only the Charging Point "CP-01" is returned

  @US2.4 @MVP2
  Scenario: Filter Charging Points by Charging Mode and Charging State
    Given the following Charging Points exist:
      | Charging Point | Charging Mode | Charging State |
      | CP-01          | AC            | available      |
      | CP-02          | AC            | occupied       |
      | CP-03          | DC            | available      |
    When the Customer filters Charging Points by "AC" and "available"
    Then only the Charging Point "CP-01" is returned

  # US-2.5 - View Charging Locations on an interactive map
  # TODO: Clarify the scope of this User Story.

  # Error Cases

  @US2.1 @MVP1 @error
  Scenario: Request an unknown Charging Location
    When the Customer requests the Charging Points of "Unknown Location"
    Then an error message "Unknown Charging Location" is returned

  @US2.2 @MVP1 @error
  Scenario: Request an unknown Charging Point
    When the Customer requests the Charging State of "CP-99"
    Then an error message "Unknown Charging Point" is returned
