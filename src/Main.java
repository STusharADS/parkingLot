import java.time.LocalDateTime;
import parkinglot.Bill;
import parkinglot.DemoData;
import parkinglot.ParkingLot;
import parkinglot.ParkingTicket;
import parkinglot.SlotType;
import parkinglot.VehicleDetails;
import parkinglot.VehicleType;

public class Main {
    public static void main(String[] args) {
        ParkingLot pl = DemoData.sampleLot();

        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 10, 0);
        VehicleDetails bike = new VehicleDetails("UP16-AA-1111", VehicleType.TWO_WHEELER);

        ParkingTicket tkt = pl.park(bike, t0, SlotType.SMALL, "G1");
        System.out.println("ticketId=" + tkt.getTicketId() + ", slot=" + tkt.getSlotNo() + ", type=" + tkt.getSlotType());

        Bill bill = pl.exit(tkt, t0.plusHours(2).plusMinutes(20));
        System.out.println("billAmt=" + bill.getTotalAmt() + ", billedHrs=" + bill.getBilledHrs());
    }
}
