package parkinglot;

import java.util.List;
import java.util.Map;

public final class DemoData {
    private DemoData() {}

    public static ParkingLot sampleLot() {
        List<String> gates = List.of("G1", "G2");

        List<ParkingSlot> slots = List.of(
            new ParkingSlot("S1", SlotType.SMALL, 1, Map.of("G1", 2.0, "G2", 8.0)),
            new ParkingSlot("S2", SlotType.SMALL, 2, Map.of("G1", 6.0, "G2", 2.0)),
            new ParkingSlot("M1", SlotType.MEDIUM, 1, Map.of("G1", 3.0, "G2", 7.0)),
            new ParkingSlot("M2", SlotType.MEDIUM, 2, Map.of("G1", 7.0, "G2", 3.0)),
            new ParkingSlot("L1", SlotType.LARGE, 1, Map.of("G1", 5.0, "G2", 6.0)),
            new ParkingSlot("L2", SlotType.LARGE, 2, Map.of("G1", 9.0, "G2", 4.0))
        );

        Map<SlotType, Double> rates = Map.of(
            SlotType.SMALL, 20.0,
            SlotType.MEDIUM, 40.0,
            SlotType.LARGE, 80.0
        );

        return new ParkingLot(slots, gates, rates);
    }
}
