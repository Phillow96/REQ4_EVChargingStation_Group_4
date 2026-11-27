
@EP-8 @Owner
Feature: EP-8 Owner Customer Account Management

  As the Owner of the EV Charging Network
  I want to access registered Prepaid Accounts and view their balances
  so that I can monitor customer accounts and their available prepaid funds.

  # User Stories:
  # US-8.1 Access registered Prepaid Accounts
  # US-8.2 See each customer's current Account Balance
  # CRUD operation: READ

  Background:
    Given the EV Charging Network "Vienna EV Power" is in operation

  # ============== US-8.1 READ ==============

  @US-8.1 @read
  Scenario: Owner accesses registered Prepaid Accounts
    Given the following Customers complete the Registration:
      | Anna Berger  |
      | Ada Lovelace |
      | Max Muster   |
      | Tim Messer   |
      | Eva Weiss    |
    When the Owner requests all registered Prepaid Accounts
    Then the Owner receives 5 Prepaid Accounts
    And the Owner can access the Prepaid Account of "Anna Berger"
    And the Owner can access the Prepaid Account of "Eva Weiss"

  # ============== US-8.2 READ ==============

  @US-8.2 @read
  Scenario: Owner views a Customer's current Account Balance
    Given the Customer "Anna Berger" is registered in the EV Charging Network
    And the Customer "Anna Berger" tops up 50.00 EUR
    When the Owner requests the Account Balance of "Anna Berger"
    Then the Owner receives an Account Balance of 50.00 EUR

  @US-8.2 @read
  Scenario: Owner sees an updated Account Balance
    Given the Customer "Ada Lovelace" is registered in the EV Charging Network
    And the Customer "Ada Lovelace" tops up 25.00 EUR
    And the Customer "Ada Lovelace" tops up 17.00 EUR
    When the Owner requests the Account Balance of "Ada Lovelace"
    Then the Owner receives an Account Balance of 42.00 EUR
