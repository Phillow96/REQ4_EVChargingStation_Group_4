@EP-1 @Customer
Feature: EP-1 Registration and Prepaid Account

  As a Customer
  I want to register in the EV Charging Network and manage the Account Balance of my Prepaid Account
  so that I can pay for Charging Sessions in advance.

    # User Story Map: narrative step "Registration & Account" (Tasks 1.1 and 1.2)
    # User Stories: US-1.1 Registration, US-1.2 Top-up, US-1.3 View Account Balance
    # CRUD on the static business domain object "Customer"

  Background:
    Given the EV Charging Network "Vienna EV Power" is in operation

      # ============== CREATE ==============
  @US-1.1 @create
  Scenario: Registration of a new Customer
    When the Customer "Anna Berger" completes the Registration
    Then the EV Charging Network holds the Customer "Anna Berger"
    And the Account Balance of the Customer "Anna Berger" is 0.00 EUR

  @US-1.1 @create
  Scenario: Registration of five Customers
    When the following Customers complete the Registration:
      | Customer     |
      | Anna Berger  |
      | Ada Lovelace |
      | Max Muster   |
      | Tim Messer   |
      | Eva Weiss    |
    Then the number of registered Customers is 5
    And the EV Charging Network holds the Customer "Eva Weiss"

      # ============== READ ==============

  @US-1.3 @read
  Scenario: Customer reads the Account Balance of a new Prepaid Account
    Given the Customer "Max Muster" is registered in the EV Charging Network
    When the Customer "Max Muster" requests the Account Balance
    Then an Account Balance of 0.00 EUR is returned

  @US-1.3 @read
  Scenario: Customer reads the Account balance after a Top-Up
    Given the Customer "Tim Messer" is registered in the EV Charging Network
    And the Customer "Tim Messer" tops up 30.00 EUR
    When the Customer "Tim Messer" requests the Account Balance
    Then an Account Balance of 30.00 EUR is returned

      # ============== UPDATE ==============
  @US-1.2 @update
  Scenario: Customer tops up the Prepaid Account
    Given the Customer "Anna Berger" is registered in the EV Charging Network
    When the Customer "Anna Berger" tops up 20.00 EUR
    Then the Account Balance of the Customer "Anna Berger" is 20.00 EUR

  @US-1.2 @update
  Scenario: Several Top-ups add up on the Prepaid Account
    Given the Customer "Ada Lovelace" is registered in the EV Charging Network
    When the Customer "Ada Lovelace" tops up 25.00 EUR
    And the Customer "Ada Lovelace" tops up 17.00 EUR
    Then the Account Balance of the Customer "Ada Lovelace" is 42.00 EUR
    
  @US-1.2 @update @edge
    Scenario: The smallest possible Top-up is accepted
      Given the Customer "Eva Weiss" is registered in the EV Charging Network
      When the Customer "Eva Weiss" tops up 5.00 EUR
      Then the Account Balance of the Customer "Eva Weiss" is 5.00 EUR


      # ============== ERROR & EDGE CASES ==============
  
  @US-1.1 @error
    Scenario: Registration without a name is refused
      When the Customer "" completes the Registration
      Then the request is refused with the message "A Customer needs a name"
      And the number of registered Customers is 0

  @US-1.1 @error
    Scenario: Registration of an already registered Customer is refused
      Given the Customer "Anna Berger" is registered in the EV Charging Network
      When the Customer "Anna Berger" completes the Registration
      Then the request is refused with the message "Customer is already registered"
      And the number of registered Customers is 0

  @US-1.3 @error
    Scenario: The Account Balance of an unknown Customer is not returned
      When the Customer "Felix Gruber" requests the Account Balance
      Then the request is refused with the message "Unknown Customer"

  @US-1.2 @error
    Scenario Outline: A Top-up with an invalid amount is refused
      Given the Customer "Anna Berger" is registered in the EV Charging Network
      When the Customer "Anna Berger" tops up 4.99 EUR
      Then the request is refused with the message "A top-up needs to be at least 5.00 EUR"
      And the Account Balance of the Customer "Anna Berger" is 0.00 EUR

      Examples:
        | Amount |
        | 0.00   |
        | -5.00  |