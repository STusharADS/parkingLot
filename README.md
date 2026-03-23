# Multilevel Parking Lot - Java

LLD assignment solution in Java with nearest-slot allocation by entry gate.

## APIs

- park(vehicleDetails, entryTime, requestedSlotType, entryGateID)
- status()
- exit(parkingTicket, exitTime)

## Rules done

- Slot types: SMALL / MEDIUM / LARGE
- 2W -> SMALL or MEDIUM or LARGE
- CAR -> MEDIUM or LARGE
- BUS -> LARGE only
- Nearest compatible slot from given entry gate
- Fallback to larger slot when needed
- Billing based on allocated slot type
- Different hourly rates by slot type

## Project layout

- src/Main.java
- src/parkinglot/*.java
- test/parkinglot/ParkingLotTest.java
- class_diagram.md

## Compile + run

```bash
javac -d out src/Main.java src/parkinglot/*.java
java -cp out Main
java -cp out ParkingLotTest
```

## Approach

- For each gate, maintain 3 min-heaps (SMALL/MEDIUM/LARGE) keyed by:
  - distance from gate
  - floor no
  - slot no
- park():
  - build compatible slot types based on vehicle + requested slot type
  - find nearest available across candidate heaps
  - mark occupied and issue ticket
- exit():
  - compute duration and ceil to hours
  - bill using allocated slot type rate
  - free slot and push it back to all gate heaps
