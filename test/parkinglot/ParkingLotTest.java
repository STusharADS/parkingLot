package parkinglot;

import java.time.LocalDateTime;

public class ParkingLotTest {
    public static void main(String[] args) {
        testNearestByGate();
        testFallbackToLarger();
        testBusOnlyLarge();
        testBillingByAllocatedSlotType();
        System.out.println("all tests passed");
    }

    private static void testNearestByGate() {
        ParkingLot pl = DemoData.sampleLot();
        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 9, 0);

        ParkingTicket t = pl.park(new VehicleDetails("B1", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");
        check("S1".equals(t.getSlotNo()), "expected S1 for nearest small from G1");
    }

    private static void testFallbackToLarger() {
        ParkingLot pl = DemoData.sampleLot();
        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 9, 0);

        pl.park(new VehicleDetails("B1", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");
        pl.park(new VehicleDetails("B2", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");
        ParkingTicket t = pl.park(new VehicleDetails("B3", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");

        check(t.getSlotType() == SlotType.MEDIUM, "bike should fallback to MEDIUM when SMALL full");
    }

    private static void testBusOnlyLarge() {
        ParkingLot pl = DemoData.sampleLot();
        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 9, 0);

        ParkingTicket t = pl.park(new VehicleDetails("BUS1", VehicleType.BUS), t0, SlotType.LARGE, "G2");
        check(t.getSlotType() == SlotType.LARGE, "bus must get LARGE only");
    }

    private static void testBillingByAllocatedSlotType() {
        ParkingLot pl = DemoData.sampleLot();
        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 9, 0);

        pl.park(new VehicleDetails("B1", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");
        pl.park(new VehicleDetails("B2", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");
        ParkingTicket t = pl.park(new VehicleDetails("B3", VehicleType.TWO_WHEELER), t0, SlotType.SMALL, "G1");

        Bill bill = pl.exit(t, t0.plusHours(1).plusMinutes(5));
        check(bill.getSlotType() == SlotType.MEDIUM, "bill should use allocated slot type");
        check(bill.getBilledHrs() == 2, "1h5m should bill 2 hrs");
        check(bill.getTotalAmt() == 80.0, "MEDIUM rate 40 x 2 = 80");
    }

    private static void check(boolean ok, String msg) {
        if (!ok) {
            throw new IllegalStateException(msg);
        }
    }
}
