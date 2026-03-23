package parkinglot;

import java.util.Map;

public final class ParkingSlot {
    private final String slotNo;
    private final SlotType slotType;
    private final int floorNo;
    private final Map<String, Double> gateDist;
    private boolean occupied;

    public ParkingSlot(String slotNo, SlotType slotType, int floorNo, Map<String, Double> gateDist) {
        this.slotNo = slotNo;
        this.slotType = slotType;
        this.floorNo = floorNo;
        this.gateDist = gateDist;
        this.occupied = false;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public Map<String, Double> getGateDist() {
        return gateDist;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
