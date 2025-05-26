class Rental {
    private User user;
    private Bike bike;
    private int duration;
    private double totalCost;

    public Rental(User user, Bike bike, int duration) {
        this.user = user;
        this.bike = bike;
        this.duration = duration;
        this.totalCost = calculateCost();
    }

    private double calculateCost() {
        return bike.getPricePerHour() * duration;
    }

    public User getUser() {
        return user;
    }

    public Bike getBike() {
        return bike;
    }

    public int getDuration() {
        return duration;
    }

    public double getTotalCost() {
        return totalCost;
    }
}