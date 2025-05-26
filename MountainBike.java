class MountainBike extends Bike {
    public MountainBike(String name, double pricePerHour) {
        super(name, pricePerHour);
    }

    @Override
    public BikeType getType() {
        return BikeType.MOUNTAIN;
    }
}