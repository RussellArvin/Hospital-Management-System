package enums;

/**
 * Enum representing different actions that can be taken for an appointment.
 * Each action corresponds to a specific operation in the appointment management process.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public enum AppointmentAction {
    /** Action to reschedule an appointment to a different time. */
    RESCHEDULE,

    /** Action to cancel an existing appointment. */
    CANCEL,

    /** Action to approve an appointment request. */
    APPROVE,

    /** Action to view appointment details without making changes. */
    VIEW,

    /** Action to record or update the outcome of an appointment. */
    OUTCOME
}
