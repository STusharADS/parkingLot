package parkinglot;

public enum SlotType {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private final int rank;

    SlotType(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }
}
