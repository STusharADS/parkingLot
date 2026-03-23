package parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public final class ParkingLot {
    private final Map<String, ParkingSlot> slots;
    private final Set<String> gateIds;
    private final Map<SlotType, Double> hourlyRates;
    private final Map<String, ParkingTicket> activeTickets;
    private final Map<String, Map<SlotType, PriorityQueue<SlotRef>>> avail;
    private long seq;

    public ParkingLot(List<ParkingSlot> allSlots, List<String> gateIds, Map<SlotType, Double> hourlyRates) {
        this.slots = new HashMap<>();
        this.gateIds = new HashSet<>(gateIds);
        this.hourlyRates = new EnumMap<>(SlotType.class);
        this.hourlyRates.putAll(hourlyRates);
        this.activeTickets = new HashMap<>();
        this.avail = new HashMap<>();
        this.seq = 0;

        for (String gate : gateIds) {
            Map<SlotType, PriorityQueue<SlotRef>> byType = new EnumMap<>(SlotType.class);
            byType.put(SlotType.SMALL, new PriorityQueue<>());
            byType.put(SlotType.MEDIUM, new PriorityQueue<>());
            byType.put(SlotType.LARGE, new PriorityQueue<>());
            avail.put(gate, byType);
        }

        for (ParkingSlot slot : allSlots) {
            slots.put(slot.getSlotNo(), slot);
            for (String gate : gateIds) {
                Double d = slot.getGateDist().get(gate);
                if (d == null) {
                    throw new IllegalArgumentException("Missing gate dist for slot=" + slot.getSlotNo() + ", gate=" + gate);
                }
                avail.get(gate).get(slot.getSlotType()).offer(new SlotRef(d, slot.getFloorNo(), slot.getSlotNo()));
            }
        }
    }

    public ParkingTicket park(VehicleDetails vehicleDetails, LocalDateTime entryTime, SlotType requestedSlotType, String entryGateID) {
        if (!gateIds.contains(entryGateID)) {
            throw new IllegalArgumentException("invalid gate");
        }

        List<SlotType> candidateTypes = new ArrayList<>();
        for (SlotType st : compatible(vehicleDetails.getVehicleType())) {
            if (st.rank() >= requestedSlotType.rank()) {
                candidateTypes.add(st);
            }
        }

        if (candidateTypes.isEmpty()) {
            throw new IllegalArgumentException("req slot type invalid for vehicle");
        }

        Pick pick = pickNearest(entryGateID, candidateTypes);
        if (pick == null) {
            throw new IllegalStateException("no slot available");
        }

        ParkingSlot slot = slots.get(pick.slotNo);
        slot.setOccupied(true);

        seq++;
        ParkingTicket tkt = new ParkingTicket(
            String.format("TKT-%05d", seq),
            vehicleDetails,
            slot.getSlotNo(),
            slot.getSlotType(),
            entryTime,
            entryGateID
        );
        activeTickets.put(tkt.getTicketId(), tkt);
        return tkt;
    }

    public Map<SlotType, Integer> status() {
        Map<SlotType, Integer> out = new EnumMap<>(SlotType.class);
        out.put(SlotType.SMALL, 0);
        out.put(SlotType.MEDIUM, 0);
        out.put(SlotType.LARGE, 0);

        for (ParkingSlot s : slots.values()) {
            if (!s.isOccupied()) {
                out.put(s.getSlotType(), out.get(s.getSlotType()) + 1);
            }
        }
        return out;
    }

    public Bill exit(ParkingTicket parkingTicket, LocalDateTime exitTime) {
        ParkingTicket active = activeTickets.remove(parkingTicket.getTicketId());
        if (active == null) {
            throw new IllegalArgumentException("ticket not active");
        }
        if (exitTime.isBefore(active.getEntryTime())) {
            throw new IllegalArgumentException("exit < entry");
        }

        long mins = Duration.between(active.getEntryTime(), exitTime).toMinutes();
        if (mins <= 0) {
            mins = 1;
        }
        long billedHrs = (mins + 59) / 60;

        double rate = hourlyRates.get(active.getSlotType());
        double amt = billedHrs * rate;

        ParkingSlot slot = slots.get(active.getSlotNo());
        slot.setOccupied(false);

        for (String gate : gateIds) {
            double dist = slot.getGateDist().get(gate);
            avail.get(gate).get(slot.getSlotType()).offer(new SlotRef(dist, slot.getFloorNo(), slot.getSlotNo()));
        }

        return new Bill(
            active.getTicketId(),
            active.getSlotType(),
            rate,
            mins,
            billedHrs,
            amt
        );
    }

    private Pick pickNearest(String gateId, List<SlotType> types) {
        for (SlotType st : types) {
            PriorityQueue<SlotRef> pq = avail.get(gateId).get(st);
            while (!pq.isEmpty()) {
                SlotRef top = pq.peek();
                ParkingSlot s = slots.get(top.slotNo);
                if (s.isOccupied()) {
                    pq.poll();
                    continue;
                }

                SlotRef cur = pq.poll();
                if (!slots.get(cur.slotNo).isOccupied()) {
                    return new Pick(st, cur.dist, cur.floorNo, cur.slotNo);
                }
            }
        }
        return null;
    }

    private List<SlotType> compatible(VehicleType vt) {
        if (vt == VehicleType.TWO_WHEELER) {
            return List.of(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE);
        }
        if (vt == VehicleType.CAR) {
            return List.of(SlotType.MEDIUM, SlotType.LARGE);
        }
        return List.of(SlotType.LARGE);
    }

    private static final class SlotRef implements Comparable<SlotRef> {
        private final double dist;
        private final int floorNo;
        private final String slotNo;

        private SlotRef(double dist, int floorNo, String slotNo) {
            this.dist = dist;
            this.floorNo = floorNo;
            this.slotNo = slotNo;
        }

        @Override
        public int compareTo(SlotRef o) {
            return Comparator
                .comparingDouble((SlotRef x) -> x.dist)
                .thenComparingInt(x -> x.floorNo)
                .thenComparing(x -> x.slotNo)
                .compare(this, o);
        }
    }

    private static final class Pick implements Comparable<Pick> {
        private final SlotType slotType;
        private final double dist;
        private final int floorNo;
        private final String slotNo;

        private Pick(SlotType slotType, double dist, int floorNo, String slotNo) {
            this.slotType = slotType;
            this.dist = dist;
            this.floorNo = floorNo;
            this.slotNo = slotNo;
        }

        @Override
        public int compareTo(Pick o) {
            return Comparator
                .comparingDouble((Pick x) -> x.dist)
                .thenComparingInt(x -> x.floorNo)
                .thenComparing(x -> x.slotNo)
                .compare(this, o);
        }
    }
}
