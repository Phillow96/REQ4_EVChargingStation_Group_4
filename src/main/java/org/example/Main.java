package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EVChargingNetwork network = new EVChargingNetwork("Group 4");
        CustomerManager customers = network.getCustomerManager();

        ChargingLocationManager locationManager =
                network.getChargingLocationManager();

        ChargingPointManager pointManager =
                network.getChargingPointManager();

        // Fiktive Charging Locations zur Demonstration
        locationManager.createChargingLocation("Vienna Central", "Test Street 1");
        locationManager.createChargingLocation("Vienna Floridsdorf", "Test Street 2");

        // Fiktive Charging Points zur Demonstration
        ChargingLocation central =
                locationManager.readChargingLocation("Vienna Central");

        ChargingLocation floridsdorf =
                locationManager.readChargingLocation("Vienna Floridsdorf");

        pointManager.createChargingPoint("CP-01", "AC", central);
        pointManager.createChargingPoint("CP-02", "DC", central);

        pointManager.createChargingPoint("CP-03", "AC", floridsdorf);
        pointManager.createChargingPoint("CP-04", "DC", floridsdorf);

        pointManager.updateChargingState("CP-02", "occupied");
        pointManager.updateChargingState("CP-03", "out of order");


        System.out.println("Welcome to the EV-Charging Network: " + network.getNetworkName());
        System.out.println();
        // -- Main menu --
        System.out.println("What would you like to do today? ");
        System.out.println("0 -> Register");
        System.out.println("1 -> Display Account Balance");
        System.out.println("2 -> Exit");
        System.out.println("3 -> View Charging Locations");
        System.out.println("4 -> View Charging Points of a Charging Location");
        System.out.println("5 -> View Charging State of a Charging Point");


        System.out.print("Enter a number here: ");
        int input = sc.nextInt();
        switch (input) {
            case 0:
                register(network);
                break;
            case 1:
                String nameInput = enterName();

                if (customers.readCustomer(nameInput) != null) {
                    System.out.println(customers.readCustomer(nameInput).getPrepaidAccount().getBalance());
                    break;
                }
                System.out.println("Customer is not registered in this system.");
                break;
            case 2:
                System.out.println("Goodbye!");
                System.exit(0);
            case 3:
                System.out.println("Charging Locations:");

                for (ChargingLocation location : locationManager.readChargingLocations()) {
                    System.out.println(location);
                }
                break;
            case 4:
                sc.nextLine();

                System.out.print("Enter the name of the Charging Location: ");
                String locationName = sc.nextLine();

                ChargingLocation selectedLocation =
                        locationManager.readChargingLocation(locationName);

                if (selectedLocation != null) {
                    System.out.println("Charging Points:");

                    for (ChargingPoint point : pointManager.readChargingPoints(selectedLocation)) {
                        System.out.println(point);
                    }
                } else {
                    System.out.println("Unknown Charging Location.");
                }
                break;
            case 5:
                sc.nextLine();

                System.out.print("Enter the name of the Charging Point: ");
                String pointName = sc.nextLine();

                try {
                    System.out.println("Charging State: "
                            + pointManager.readChargingState(pointName));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Unknown status");

        }
    }

    public static void register(EVChargingNetwork network){

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

        System.out.println("Thank you, " + customer.getName() + " for registering.");
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
                    System.out.println("Your balance is: " + customer.getPrepaidAccount().getBalance() + " EUR.");
                    break;
                }
                System.out.println("Invalid input.");
                break;
            case 1:
                System.out.println("Your balance is: ");
                System.out.println(customer.getPrepaidAccount().getBalance());

            default:
                System.out.println("Not a valid service.");
        }
    }

}
