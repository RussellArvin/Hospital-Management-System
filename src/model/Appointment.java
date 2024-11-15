package model;

import enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * The Appointment class represents a scheduled appointment between a doctor and a patient.
 * It includes information about the participants, timing, status, and cancellation details.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0 
 */
public class Appointment extends BaseEntity {

    private String doctorId;
    private String patientId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus status;
    private String cancelReason;

    /**
     * Constructs an Appointment with the specified doctor, patient, start and end times.
     * The appointment status is initially set to REQUESTED, and the cancellation reason is null.
     *
     * @param doctorId      The ID of the doctor associated with this appointment.
     * @param patientId     The ID of the patient associated with this appointment.
     * @param startDateTime The starting date and time of the appointment.
     * @param endDateTime   The ending date and time of the appointment.
     */
    public Appointment(
        String doctorId,
        String patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        super(BaseEntity.generateUUID());

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = AppointmentStatus.REQUESTED;
        this.cancelReason = null;
    }

    /**
     * Constructs an Appointment with detailed information including an ID, status, and timestamps.
     *
     * @param id            The unique ID of the appointment.
     * @param doctorId      The ID of the doctor associated with this appointment.
     * @param patientId     The ID of the patient associated with this appointment.
     * @param startDateTime The starting date and time of the appointment.
     * @param endDateTime   The ending date and time of the appointment.
     * @param status        The current status of the appointment.
     * @param cancelReason  The reason for cancellation, if applicable.
     * @param createdAt     The date and time when this appointment was created.
     * @param updatedAt     The date and time when this appointment was last updated.
     */
    public Appointment(
        String id,
        String doctorId,
        String patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        AppointmentStatus status,
        String cancelReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the start date and time of the appointment.
     *
     * @return The starting date and time of the appointment.
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param date The starting date and time to set.
     */
    public void setStartDateTime(LocalDateTime date) {
        this.startDateTime = date;
    }

    /**
     * Gets the end date and time of the appointment.
     *
     * @return The ending date and time of the appointment.
     */
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    /**
     * Sets the end date and time of the appointment.
     *
     * @param date The ending date and time to set.
     */
    public void setEndDateTime(LocalDateTime date) {
        this.endDateTime = date;
    }

    /**
     * Gets the current status of the appointment.
     *
     * @return The status of the appointment.
     */
    public AppointmentStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the appointment.
     *
     * @param status The status to set for the appointment.
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Sets the cancellation reason for the appointment.
     *
     * @param cancelReason The reason for cancellation.
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * Gets the ID of the patient associated with the appointment.
     *
     * @return The patient's ID.
     */
    public String getPatientId() {
        return this.patientId;
    }

    /**
     * Gets the ID of the doctor associated with the appointment.
     *
     * @return The doctor's ID.
     */
    public String getDoctorId() {
        return this.doctorId;
    }

    /**
     * Gets the reason for cancellation of the appointment.
     *
     * @return The cancellation reason, or null if not cancelled.
     */
    public String getCancelReason() {
        return this.cancelReason;
    }

    /**
     * Returns a CSV representation of the appointment data, including escape handling for commas.
     *
     * @return A CSV string containing appointment data.
     */
    @Override
    public String toCsvString() {
        return String.join(",",
            id,
            doctorId,
            patientId,
            startDateTime.toString(),
            endDateTime.toString(),
            status.toString(),
            cancelReason != null ? cancelReason.replace(",", ";") : "",
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
