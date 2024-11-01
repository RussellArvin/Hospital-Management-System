package enums;

public enum AppointmentStatus {
    REQUESTED,     // Initial state when patient books
    CONFIRMED,     // Doctor accepted
    DOCTOR_CANCELLED,
    PATIENT_CANCELLED,
    COMPLETED      // Appointment finished
}