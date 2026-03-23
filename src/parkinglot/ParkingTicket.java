package parkinglot;

import java.time.LocalDateTime;

public final class ParkingTicket {
    private final String ticketId;
    private final VehicleDetails vehicle;
    private final String slotNo;
    private final SlotType slotType;
    private final LocalDateTime entryTime;
    private final String entryGateId;

    public ParkingTicket(
        String ticketId,
        VehicleDetails vehicle,
        String slotNo,
        SlotType slotType,
        LocalDateTime entryTime,
        String entryGateId
    ) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slotNo = slotNo;
        this.slotType = slotType;
        this.entryTime = entryTime;
        this.entryGateId = entryGateId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public VehicleDetails getVehicle() {
        return vehicle;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getEntryGateId() {
        return entryGateId;
    }
}
