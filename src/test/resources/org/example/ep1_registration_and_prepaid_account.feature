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

      @US-1.1 @create
        Scenario: Registration of a new Customer
          When the Customer "Anna Berger" completes the Registration
          Then the EV Charging Network holds the Customer "Anna Berger"
          And the Account Balance of the Customer "Anna Berger" is 0.00 EUR