
package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EVChargingNetwork network = new EVChargingNetwork("Group 4");
        CustomerManager customers = network.getCustomerManager();

        System.out.println("Welcome to the EV-Charging Network: "
                + network.getNetworkName());
        System.out.println();

        // Main menu
        System.out.println("What would you like to do today?");
        System.out.println("0 -> Register");
        System.out.println("1 -> Display Account Balance");
        System.out.println("2 -> Exit");
        System.out.println("3 -> Owner: View Customer Accounts");

        System.out.print("Enter a number here: ");
        int input = sc.nextInt();

        switch (input) {
            case 0:
                register(network);
                break;

            case 1:
                String nameInput = enterName();

                if (customers.readCustomer(nameInput) != null) {
                    System.out.println(customers.readCustomer(nameInput)
                            .getPrepaidAccount().getBalance());
                    break;
                }

                System.out.println("Customer is not registered in this system.");
                break;

            case 2:
                System.out.println("Goodbye!");
                break;

            case 3:
                ownerFeatures(customers);
                break;

            default:
                System.out.println("Unknown status");
        }
    }

    // Existing customer functionality

    public static void register(EVChargingNetwork network) {
        String nameInput = enterName();
        network.getCustomerManager().registerCustomer(nameInput);
        customerFeatures(network.getCustomerManager().readCustomer(nameInput));
    }

    public static String enterName() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        return sc.nextLine();
    }

    public static void customerFeatures(Customer customer) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Thank you, " + customer.getName()
                + " for registering.");
        System.out.println();
        System.out.println("What service would you like to access?");
        System.out.println("0 -> Add credit to balance");
        System.out.println("1 -> Display Balance");

        System.out.print("Enter a number here: ");
        int input = sc.nextInt();

        switch (input) {
            case 0:
                System.out.print("How much would you like to add?: ");
                double amount = sc.nextDouble();

                if (amount > 0) {
                    customer.getPrepaidAccount().topUp(amount);
                    System.out.println("Thank you!");
                    System.out.println("Your balance is: "
                            + customer.getPrepaidAccount().getBalance()
                            + " EUR.");
                    break;
                }

                System.out.println("Invalid input.");
                break;

            case 1:
                System.out.println("Your balance is: ");
                System.out.println(customer.getPrepaidAccount().getBalance());
                break;

            default:
                System.out.println("Not a valid service.");
        }
    }

    // ==========================================
    // EP-8: OWNER CUSTOMER ACCOUNT MANAGEMENT
    // ==========================================

    public static void ownerFeatures(CustomerManager customers) {

        System.out.println();
        System.out.println("=== OWNER CUSTOMER ACCOUNT MANAGEMENT ===");

        // Create five example customers for the demonstration
        String[] names = {
                "Anna Berger",
                "Ada Lovelace",
                "Max Muster",
                "Tim Messer",
                "Eva Weiss"
        };

        double[] balances = {
                50.00,
                42.00,
                25.00,
                100.00,
                0.00
        };

        for (int i = 0; i < names.length; i++) {
            if (!customers.holdsCustomer(names[i])) {
                customers.registerCustomer(names[i]);
                customers.readCustomer(names[i])
                        .getPrepaidAccount()
                        .topUp(balances[i]);
            }
        }

        // US-8.1: Access registered prepaid accounts
        System.out.println();
        System.out.println("US-8.1: Registered Prepaid Accounts");
        System.out.println("Total registered customers: "
                + customers.getRegisteredCustomers());

        // US-8.2: See each customer's current account balance
        System.out.println();
        System.out.println("US-8.2: Current Customer Account Balances");

        for (Customer customer : customers.getAllCustomers()) {
            System.out.printf("%-20s | Balance: %.2f EUR%n",
                    customer.getName(),
                    customer.getPrepaidAccount().getBalance());
        }

        System.out.println();
        System.out.println("Owner account overview completed.");
    }
}
