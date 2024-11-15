package enums;

/**
 * Enum representing the various statuses of an appointment in the system.
 * Each status reflects the current state of the appointment lifecycle.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public enum AppointmentStatus {
    /** Initial state when a patient has requested or booked an appointment. */
    REQUESTED,     

    /** Status indicating that the doctor has confirmed the appointment. */
    CONFIRMED,     

    /** Status indicating that the doctor has canceled the appointment. */
    DOCTOR_CANCELLED,

    /** Status indicating that the patient has canceled the appointment. */
    PATIENT_CANCELLED,

    /** Status indicating that the appointment has been completed. */
    COMPLETED      
}
