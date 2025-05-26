import java.util.UUID;

enum BikeType {
    MOUNTAIN, FOLDING, ELECTRIC
}

abstract class Bike {
    private String id;
    private String name;
    private double pricePerHour;
    private boolean available;

    public Bike(String name, double pricePerHour) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract BikeType getType();
}