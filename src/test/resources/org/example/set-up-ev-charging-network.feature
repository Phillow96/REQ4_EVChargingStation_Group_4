@CPO @EP5
Feature: Set up the EV Charging Network

  As a CPO
  I want to set up and maintain the Charging Locations and Charging Points of my EV Charging Network
  so that I can operate and expand my network.

  # Story Map: Set up the charging network -> Task 1 & Task 2
  # Scope tags: @MVP1 = static setup (US-5.1 to US-5.4)
  # This Feature File is independent: every Charging Location and Charging Point it needs is created inside the file.

  Background:
    Given the EV Charging Network "Vienna EV Power" is in operation

  # ---------------- Task 1: Charging Locations (Create, Read, Update) ----------------

  @US5.1 @Task1 @MVP1
  Scenario: CPO adds a new Charging Location
    When the CPO adds the Charging Location "Stephansplatz Hub" at "Stephansplatz 1, 1010 Wien"
    Then the Charging Location "Stephansplatz Hub" exists in the EV Charging Network
    And the address of the Charging Location "Stephansplatz Hub" is "Stephansplatz 1, 1010 Wien"

  @US5.1 @Task1 @MVP1
  Scenario: CPO maintains an existing Charging Location
    Given the Charging Location "Praterstern Nord" at "Praterstern 1, 1020 Wien" exists in the EV Charging Network
    When the CPO updates the address of the Charging Location "Praterstern Nord" to "Praterstern 5, 1020 Wien"
    Then the address of the Charging Location "Praterstern Nord" is "Praterstern 5, 1020 Wien"

  @US5.2 @Task1 @MVP1
  Scenario: EV Charging Network holds at least ten Charging Locations
    When the CPO adds the following Charging Locations to the EV Charging Network:
      | Charging Location  | Address                      |
      | Stephansplatz Hub  | Stephansplatz 1, 1010 Wien   |
      | Praterstern Nord   | Praterstern 1, 1020 Wien     |
      | Hauptbahnhof Süd   | Am Hauptbahnhof 1, 1100 Wien |
      | Westbahnhof City   | Europaplatz 2, 1150 Wien     |
      | Mariahilf Center   | Mariahilfer Str. 10, 1070    |
      | Karlsplatz Opera   | Karlsplatz 4, 1010 Wien      |
      | Schwedenplatz Quay | Schwedenplatz 3, 1010 Wien   |
      | Kagraner Platz     | Kagraner Platz 8, 1220 Wien  |
      | Floridsdorf Bhf    | Franz-Jonas-Platz 12, 1210   |
      | Meidling Station   | Eichenstraße 1, 1120 Wien    |
    Then 10 Charging Locations exist in the EV Charging Network

  # ---------------- Task 2: Charging Points, Modes & States ----------------

  @US5.3 @Task2 @MVP1
  Scenario: CPO assigns 2 to 5 Charging Points with Charging Modes to Charging Locations
    Given the following Charging Locations exist in the EV Charging Network:
      | Charging Location | Address                    |
      | Stephansplatz Hub | Stephansplatz 1, 1010 Wien |
      | Praterstern Nord  | Praterstern 1, 1020 Wien   |
    When the CPO assigns the following Charging Points to the Charging Locations:
      | Charging Location | Charging Point | Charging Mode |
      | Stephansplatz Hub | CP-01          | AC            |
      | Stephansplatz Hub | CP-02          | DC            |
      | Praterstern Nord  | CP-03          | AC            |
      | Praterstern Nord  | CP-04          | AC            |
      | Praterstern Nord  | CP-05          | DC            |
      | Praterstern Nord  | CP-06          | DC            |
      | Praterstern Nord  | CP-07          | AC            |
    Then the Charging Location "Stephansplatz Hub" has 2 Charging Points
    And the Charging Location "Praterstern Nord" has 5 Charging Points

  @US5.3 @Task2 @MVP1
  Scenario: Assigned Charging Points store their Charging Mode
    Given the Charging Location "Hauptbahnhof Süd" at "Am Hauptbahnhof 1, 1100 Wien" exists in the EV Charging Network
    When the CPO assigns the following Charging Points to "Hauptbahnhof Süd":
      | Charging Point | Charging Mode |
      | CP-10          | AC            |
      | CP-11          | DC            |
      | CP-12          | DC            |
    Then the following Charging Points exist at "Hauptbahnhof Süd":
      | Charging Point | Charging Mode |
      | CP-10          | AC            |
      | CP-11          | DC            |
      | CP-12          | DC            |

  @US5.4 @Task2 @MVP1
  Scenario: CPO sees the Charging State of each Charging Point
    Given the Charging Location "Westbahnhof City" at "Europaplatz 2, 1150 Wien" exists in the EV Charging Network
    And "Westbahnhof City" has the following Charging Points:
      | Charging Point | Charging Mode | Charging State |
      | CP-20          | AC            | available      |
      | CP-21          | DC            | occupied       |
      | CP-22          | DC            | out of order   |
    When the CPO requests the Charging Points of "Westbahnhof City"
    Then the following Charging Points are returned:
      | Charging Point | Charging Mode | Charging State |
      | CP-20          | AC            | available      |
      | CP-21          | DC            | occupied       |
      | CP-22          | DC            | out of order   |

  # ---------------- Error and Edge Cases ----------------

  @US5.1 @Task1 @MVP1 @error
  Scenario: Adding a duplicate Charging Location is refused
    Given the Charging Location "Stephansplatz Hub" at "Stephansplatz 1, 1010 Wien" exists in the EV Charging Network
    When the CPO adds the Charging Location "Stephansplatz Hub" at "Stephansplatz 1, 1010 Wien"
    Then the request is refused with the message "Charging Location already exists"

  @US5.3 @Task2 @MVP1 @error
  Scenario: Assigning a Charging Point to an unknown Charging Location is refused
    When the CPO assigns Charging Point "CP-99" with Charging Mode "AC" to "Unknown Location"
    Then the request is refused with the message "Unknown