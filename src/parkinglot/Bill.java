package parkinglot;

public final class Bill {
    private final String ticketId;
    private final SlotType slotType;
    private final double hourlyRate;
    private final long parkedMins;
    private final long billedHrs;
    private final double totalAmt;

    public Bill(String ticketId, SlotType slotType, double hourlyRate, long parkedMins, long billedHrs, double totalAmt) {
        this.ticketId = ticketId;
        this.slotType = slotType;
        this.hourlyRate = hourlyRate;
        this.parkedMins = parkedMins;
        this.billedHrs = billedHrs;
        this.totalAmt = totalAmt;
    }

    public String getTicketId() {
        return ticketId;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public long getParkedMins() {
        return parkedMins;
    }

    public long getBilledHrs() {
        return billedHrs;
    }

    public double getTotalAmt() {
        return totalAmt;
    }
}
