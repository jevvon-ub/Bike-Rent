import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalManager {
    private List<Bike> bikes;
    private Map<Integer, Rental> rentalHistory;

    public RentalManager() {
        this.bikes = new ArrayList<>();
        this.rentalHistory = new HashMap<>();
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void listAvailableBikes() {
        for (Bike bike : bikes) {
            System.out.println("--------------------------");
            bike.getInfo();
            // System.out.println("--------------------------");
        }
    }

    public void rentBike(User user, String bikeId, int duration) {
        Bike selectedBike = null;
        for (Bike bike : bikes) {
            if (bike.getId().equals(bikeId)) {
                selectedBike = bike;
                break;
            }
        }

        if (selectedBike == null) {
            System.out.println("\n--------------------------");
            System.out.println(Main.RED_COLOR + "Bike not found." + Main.RESET_COLOR);
            System.out.println("--------------------------");
            return;
        }

        Rental rental = new Rental(user, selectedBike, duration);
        rental.generateReceipt();
        rentalHistory.put(rentalHistory.size() + 1, rental);
    }

    public void showRentalHistory() {
        for (Map.Entry<Integer, Rental> entry : rentalHistory.entrySet()) {
            System.out.println("Rental ID: " + entry.getKey());
            entry.getValue().generateReceipt();
            System.out.println("---------------------------");
        }
    }
}
