package enums;

public enum BloodType {
    O_POSITIVE,
    A_POSITIVE,
    B_POSITIVE,
    AB_POSITIVE,
    O_NEGATIVE,
    A_NEGATIVE,
    B_NEGATIVE,
    AB_NEGATIVE;

    // Optional: If you want to display it as O+, A+, etc.
    @Override
    public String toString() {
        switch(this) {
            case O_POSITIVE: return "O+";
            case A_POSITIVE: return "A+";
            case B_POSITIVE: return "B+";
            case AB_POSITIVE: return "AB+";
            case O_NEGATIVE: return "O-";
            case A_NEGATIVE: return "A-";
            case B_NEGATIVE: return "B-";
            case AB_NEGATIVE: return "AB-";
            default: return "UNKNOWN";
        }
    }
}