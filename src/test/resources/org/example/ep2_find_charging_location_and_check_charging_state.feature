
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
      | Charging Location  | Address       |
      | Vienna Central     | Test Street 1 |
      | Vienna Floridsdorf | Test Street 2 |
    When the Customer requests all Charging Locations
    Then 2 Charging Locations are returned
    And the Charging Location "Vienna Central" is returned
    And the Charging Location "Vienna Floridsdorf" is returned

  @US2.1 @MVP1
  Scenario: View Charging Points of a Charging Location
    Given the Charging Location "Vienna Central" exists
    And the Charging Location has the following Charging Points:
      | Charging Point | Charging Mode |
      | CP-01          | AC            |
      | CP-02          | DC            |
    When the Customer requests the Charging Points of "Vienna Central"
    Then 2 Charging Points are returned
    And the Charging Point "CP-01" with Charging Mode "AC" is returned
    And the Charging Point "CP-02" with Charging Mode "DC" is returned

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
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |
    When the Customer requests the Pricing Rates of "Vienna Central"
    Then the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |

  @US2.3 @MVP2
  Scenario: Pricing Rates differ between Charging Locations
    Given the Charging Location "Vienna Central" exists
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |
    And the Charging Location "Vienna Floridsdorf" exists
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.40        | 0.03         |
      | DC            | 0.60        | 0.06         |
    When the Customer requests the Pricing Rates of "Vienna Floridsdorf"
    Then the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.40        | 0.03         |
      | DC            | 0.60        | 0.06         |

  @US2.3 @MVP2
  Scenario: View the currently valid Pricing Rates
    Given the Charging Location "Vienna Central" exists
    And the Charging Location has the following scheduled Pricing Rates:
      | Pricing Rates ID | Valid From Offset | AC Energy Rate | DC Energy Rate | AC Parking Rate | DC Parking Rate |
      | PR-01            | -120              | 0.35           | 0.55           | 0.02            | 0.05            |
      | PR-03            | 60                | 0.55           | 0.75           | 0.04            | 0.07            |
      | PR-02            | -60               | 0.45           | 0.65           | 0.03            | 0.06            |
    When the Customer requests the Pricing Rates of "Vienna Central"
    Then the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.45        | 0.03         |
      | DC            | 0.65        | 0.06         |

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

  # US-2.6 - View the details of a Charging Location

  @US2.6 @MVP1
  Scenario: View details of a Charging Location
    Given the Charging Location "Vienna Central" with Address "Test Street 1" exists
    And the Charging Location has the following Charging Points:
      | Charging Point | Charging Mode |
      | CP-01          | AC            |
      | CP-02          | DC            |
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |
    When the Customer requests the details of "Vienna Central"
    Then the Address is "Test Street 1"
    And 2 Charging Points are returned
    And the Charging Point "CP-01" with Charging Mode "AC" is returned
    And the Charging Point "CP-02" with Charging Mode "DC" is returned
    And the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |

  @US2.6 @MVP1
  Scenario: View details of a specific Charging Location
    Given the Charging Location "Vienna Central" with Address "Test Street 1" exists
    And the Charging Location has the following Charging Points:
      | Charging Point | Charging Mode |
      | CP-01          | AC            |
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.35        | 0.02         |
      | DC            | 0.55        | 0.05         |
    And the Charging Location "Vienna Floridsdorf" with Address "Test Street 2" exists
    And the Charging Location has the following Charging Points:
      | Charging Point | Charging Mode |
      | CP-03          | DC            |
    And the Charging Location has the following Pricing Rates:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.40        | 0.03         |
      | DC            | 0.60        | 0.06         |
    When the Customer requests the details of "Vienna Floridsdorf"
    Then the Address is "Test Street 2"
    And 1 Charging Points are returned
    And the Charging Point "CP-03" with Charging Mode "DC" is returned
    And the following Pricing Rates are returned:
      | Charging Mode | Energy Rate | Parking Rate |
      | AC            | 0.40        | 0.03         |
      | DC            | 0.60        | 0.06         |

  # Error Cases

  @US2.1 @MVP1 @error
  Scenario: Request an unknown Charging Location
    When the Customer requests the Charging Points of "Unknown Location"
    Then an error message "Unknown Charging Location" is returned

  @US2.2 @MVP1 @error
  Scenario: Request an unknown Charging Point
    When the Customer requests the Charging State of "CP-99"
    Then an error message "Unknown Charging Point" is returned

  @US2.3 @MVP2 @error
  Scenario: Request Pricing Rates of an unknown Charging Location
    When the Customer requests the Pricing Rates of "Unknown Location"
    Then an error message "Unknown Charging Location" is returned

  @US2.3 @MVP2 @error
  Scenario: Request Pricing Rates of a Charging Location without valid rates
    Given the Charging Location "Vienna Central" exists
    When the Customer requests the Pricing Rates of "Vienna Central"
    Then an error message "No valid Pricing Rates" is returned

  @US2.6 @MVP1 @error
  Scenario: Request details of an unknown Charging Location
    When the Customer requests the details of "Unknown Location"
    Then an error message "Unknown Charging Location" is returned