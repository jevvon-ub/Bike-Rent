class ElectricBike extends Bike {
    public ElectricBike(String name, double pricePerHour) {
        super(name, pricePerHour);
    }

    @Override
    public BikeType getType() {
        return BikeType.ELECTRIC;
    }
}