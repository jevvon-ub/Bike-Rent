import java.util.*;

class RentalManager {
    private List<Bike> bikes;
    private List<Rental> rentalHistory;

    public RentalManager() {
        bikes = new ArrayList<>();
        rentalHistory = new ArrayList<>();
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public boolean rentBike(User user, String bikeId, int duration) {
        for (Bike bike : bikes) {
            if (bike.getId().equals(bikeId) && bike.isAvailable()) {
                bike.setAvailable(false);
                Rental rental = new Rental(user, bike, duration);
                rentalHistory.add(rental);
                return true;
            }
        }
        return false;
    }

    public boolean returnBike(String bikeId) {
        for (Bike bike : bikes) {
            if (bike.getId().equals(bikeId) && !bike.isAvailable()) {
                bike.setAvailable(true);
                return true;
            }
        }
        return false; // Bike not found or already available
    }

    public List<Bike> getAvailableBikes() {
        List<Bike> availableBikes = new ArrayList<>();
        for (Bike bike : bikes) {
            if (bike.isAvailable()) {
                availableBikes.add(bike);
            }
        }
        return availableBikes;
    }

    public List<Bike> getAllBikes() {
        return bikes;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }
}