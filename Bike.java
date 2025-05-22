enum BikeType {
    MOUNTAIN,
    FOLDING,
    ELECTRIC,
}

abstract class Bike {
    private String id;
    private String brand;
    private BikeType type;
    private double pricePerHour;

    public Bike(String brand, BikeType type, double pricePerHour) {
        this.id = generateUniqueId();
        this.brand = brand;
        this.type = type;
        this.pricePerHour = pricePerHour;
    }

    private String generateUniqueId() {
        String uniqueId = "";
        for (int i = 0; i < 5; i++) {
            int randomNum = (int) (Math.random() * 36);
            if (randomNum < 10) {
                uniqueId += (randomNum);
            } else {
                uniqueId += ((char) ('A' + randomNum - 10));
            }
        }

        return uniqueId;
    }

    public double calculateRentalCost(int hours) {
        return hours * pricePerHour;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public BikeType getType() {
        return type;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void getInfo() {
        System.out.println("Bike ID: " + id);
        System.out.println("Brand: " + brand);
        System.out.println("Type: " + type);
        System.out.println("Price per hour: Rp " + pricePerHour);
    }
}