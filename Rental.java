import java.time.LocalDateTime;
import java.util.UUID;

public class Rental {
    private String id;
    private User user;
    private Bike bike;
    private int duration; // in hours
    private double totalCost;
    private LocalDateTime rentalTime;

    public Rental(User user, Bike bike, int duration) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.bike = bike;
        this.duration = duration;
        this.totalCost = bike.calculateRentalCost(duration);
        this.rentalTime = LocalDateTime.now();
    }

    public void generateReceipt() {
        System.out.println("\n--------------------------");
        System.out.println(Main.RED_COLOR + "Rental Receipt" + Main.RESET_COLOR);
        System.out.println("--------------------------");

        System.out.println("Rental ID\t: " + id);
        System.out.println("Name\t\t: " + user.getName());
        System.out.println("Phone Number\t: " + user.getPhoneNumber());
        System.out.println("--------------------------");
        bike.getInfo();
        System.out.println("--------------------------");
        System.out.println("Duration\t: " + duration + " hours");
        System.out.println("Total Cost\t: Rp " + totalCost);
        System.out.println("Rental Time\t: " + rentalTime.toLocalDate().toString());
        System.out.println("--------------------------");
    }
}
