package enums;

/**
 * Enum representing the status of a prescription.
 * Used to track the progress of prescription processing.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public enum PrescriptionStatus {
    /** Prescription is pending and has not been dispensed yet. */
    PENDING,
    
    /** Prescription has been dispensed to the patient. */
    DISPENSED
}
