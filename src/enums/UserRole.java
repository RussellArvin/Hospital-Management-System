package enums;

/**
 * Enum representing different roles a user can have in the healthcare system.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0 
 */
public enum UserRole {
    /** Role for users with administrative privileges and access to system management functions. */
    ADMINISTRATOR,

    /** Role for users who are patients, accessing personal health information and appointments. */
    PATIENT,

    /** Role for users who are pharmacists, managing inventory and fulfilling prescriptions. */
    PHARMACIST,

    /** Role for users who are doctors, responsible for patient care and appointment management. */
    DOCTOR,

    /** Role for users who are nurses, assisting in patient care and handling patient records. */
    NURSE
}
