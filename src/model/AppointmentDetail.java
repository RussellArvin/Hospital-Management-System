package model;

import enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * The AppointmentDetail class extends the Appointment class, providing additional
 * details about the doctor and patient involved in the appointment.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public class AppointmentDetail extends Appointment {

    private Doctor doctor;
    private Patient patient;

    /**
     * Constructs an AppointmentDetail with the specified details.
     *
     * @param id            The unique ID of the appointment.
     * @param doctor        The doctor associated with this appointment.
     * @param patient       The patient associated with this appointment.
     * @param startDateTime The starting date and time of the appointment.
     * @param endDateTime   The ending date and time of the appointment.
     * @param status        The current status of the appointment.
     * @param cancelReason  The reason for cancellation, if applicable.
     * @param createdAt     The date and time when this appointment was created.
     * @param updatedAt     The date and time when this appointment was last updated.
     */
    public AppointmentDetail(
        String id,
        Doctor doctor,
        Patient patient,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        AppointmentStatus status,
        String cancelReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, patient.getId(), doctor.getId(), startDateTime, endDateTime, status, cancelReason, createdAt, updatedAt);
        this.doctor = doctor;
        this.patient = patient;
    }

    /**
     * Creates an AppointmentDetail object from an existing Appointment, associating it with
     * the specified doctor and patient details.
     *
     * @param appointment The existing Appointment object to use as a base.
     * @param doctor      The doctor associated with this appointment.
     * @param patient     The patient associated with this appointment.
     * @return A new AppointmentDetail object with doctor and patient details.
     */
    public static AppointmentDetail fromAppointment(
        Appointment appointment,
        Doctor doctor,
        Patient patient
    ) {
        return new AppointmentDetail(
            appointment.getId(),
            doctor,
            patient,
            appointment.getStartDateTime(),
            appointment.getEndDateTime(),
            appointment.getStatus(),
            appointment.getCancelReason(),
            appointment.getCreatedAt(),
            appointment.getUpdatedAt()    
        );
    }

    /**
     * Gets the doctor associated with this appointment.
     *
     * @return The doctor associated with this appointment.
     */
    public Doctor getDoctor(){
        return this.doctor;
    }

    /**
     * Gets the patient associated with this appointment.
     *
     * @return The patient associated with this appointment.
     */
    public Patient getPatient(){
        return this.patient;
    }
}
