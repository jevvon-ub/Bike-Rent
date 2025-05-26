class FoldingBike extends Bike {
    public FoldingBike(String name, double pricePerHour) {
        super(name, pricePerHour);
    }

    @Override
    public BikeType getType() {
        return BikeType.FOLDING;
    }
}