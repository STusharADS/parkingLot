package parkinglot;

public final class VehicleDetails {
    private final String regNo;
    private final VehicleType vehicleType;

    public VehicleDetails(String regNo, VehicleType vehicleType) {
        this.regNo = regNo;
        this.vehicleType = vehicleType;
    }

    public String getRegNo() {
        return regNo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
