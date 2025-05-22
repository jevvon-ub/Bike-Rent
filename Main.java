import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static final String RED_COLOR = "\u001B[31m"; // Red color code
    private static final String GREEN_COLOR = "\u001B[32m"; // Green color code
    public static final String RESET_COLOR = "\u001B[0m"; // Reset color code

    public static void main(String[] args) {
        RentalManager bikeRentalSystem = new RentalManager();

        bikeRentalSystem.addBike(new MountainBike("Mountain Bike", 10.0));
        bikeRentalSystem.addBike(new FoldingBike("Folding Bike", 15.0));
        bikeRentalSystem.addBike(new ElectricBike("Eelctric Bike", 12.0));

        System.out.println("Welcome to the Bike Rental System!");

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Add Bike");
            System.out.println("2. Rent Bike");
            System.out.println("3. Show List of Bikes");
            System.out.println("4. Show Rental History");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("\n--------------------------");
                    System.out.println(RED_COLOR + "Add a new bike" + RESET_COLOR);
                    System.out.println("--------------------------");

                    System.out.print("Enter bike type (Mountain/Folding/Electric): ");
                    String bikeType = scanner.next();

                    scanner.nextLine();
                    System.out.print("Enter bike name: ");
                    String bikeName = scanner.nextLine();
                    System.out.print("Enter bike price per hour: ");
                    double bikePrice = scanner.nextDouble();

                    if (bikeType.equalsIgnoreCase("Mountain")) {
                        bikeRentalSystem.addBike(new MountainBike(bikeName, bikePrice));
                    } else if (bikeType.equalsIgnoreCase("Folding")) {
                        bikeRentalSystem.addBike(new FoldingBike(bikeName, bikePrice));
                    } else if (bikeType.equalsIgnoreCase("Electric")) {
                        bikeRentalSystem.addBike(new ElectricBike(bikeName, bikePrice));
                    } else {
                        System.out.println("Invalid bike type. Please try again.");
                        continue;
                    }

                    System.out.println("--------------------------");
                    System.out.println(GREEN_COLOR + "Bike added successfully!" + RESET_COLOR);
                    System.out.println("--------------------------\n");
                    break;
                case 2:
                    System.out.println("\n--------------------------");
                    System.out.println(RED_COLOR + "Rent A Bike" + RESET_COLOR);
                    System.out.println("--------------------------");

                    System.out.print("Enter your name: ");
                    String userName = scanner.nextLine();

                    scanner.nextLine();
                    System.out.print("Enter your phone number: ");
                    String userPhone = scanner.nextLine();
                    System.out.print("Enter rental duration (in hours): ");
                    int duration = scanner.nextInt();
                    scanner.nextLine();

                    User user = new User(userName, userPhone);
                    System.out.println("\n--------------------------");
                    System.out.println(RED_COLOR + "Available Bikes" + RESET_COLOR);
                    System.out.println("--------------------------");

                    bikeRentalSystem.listAvailableBikes();
                    System.out.println("--------------------------\n");

                    System.out.print("Please choose a bike by entering its ID: ");
                    String bikeId = scanner.nextLine();

                    bikeRentalSystem.rentBike(user, bikeId, duration);
                    System.err.println();
                    break;
                case 3:
                    // Show list of bikes
                    System.out.println("\n--------------------------");
                    System.out.println(RED_COLOR + "Available Bikes" + RESET_COLOR);
                    System.out.println("--------------------------");

                    bikeRentalSystem.listAvailableBikes();
                    System.out.println("--------------------------\n");
                    break;
                case 4:
                    // Show rental history
                    System.out.println("\n--------------------------");
                    System.out.println(RED_COLOR + "Rental History" + RESET_COLOR);
                    System.out.println("--------------------------");

                    bikeRentalSystem.showRentalHistory();
                    System.out.println("--------------------------\n");
                    break;
                case 5:
                    // Exit the program
                    System.out.println("Thank you for using the Bike Rental System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
