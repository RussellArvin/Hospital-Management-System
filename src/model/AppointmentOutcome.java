package model;

import enums.AppointmentServiceType;
import java.time.LocalDateTime;

/**
 * The AppointmentOutcome class represents the outcome of an appointment, including details
 * such as the patient, appointment, type of service provided, and consultation notes.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public class AppointmentOutcome extends BaseEntity {

    private String patientId;
    private String appointmentId;
    private AppointmentServiceType serviceType;
    private String consultationNotes;

    /**
     * Constructs an AppointmentOutcome with the specified details.
     *
     * @param id               The unique ID of the appointment outcome.
     * @param patientId        The ID of the patient associated with this outcome.
     * @param appointmentId    The ID of the appointment associated with this outcome.
     * @param serviceType      The type of service provided during the appointment.
     * @param consultationNotes Notes from the consultation, describing the outcome.
     */
    public AppointmentOutcome(
       String id,
       String patientId,
       String appointmentId,
       AppointmentServiceType serviceType,
       String consultationNotes
    ) {
        super(id);
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Constructs an AppointmentOutcome with detailed information including creation and update timestamps.
     *
     * @param id               The unique ID of the appointment outcome.
     * @param patientId        The ID of the patient associated with this outcome.
     * @param appointmentId    The ID of the appointment associated with this outcome.
     * @param serviceType      The type of service provided during the appointment.
     * @param consultationNotes Notes from the consultation, describing the outcome.
     * @param createdAt        The date and time when this outcome was created.
     * @param updatedAt        The date and time when this outcome was last updated.
     */
    public AppointmentOutcome(
        String id,
        String patientId,
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Gets the ID of the patient associated with this appointment outcome.
     *
     * @return The patient's ID.
     */
    public String getPatientId() {
        return this.patientId;
    }

    /**
     * Gets the ID of the appointment associated with this outcome.
     *
     * @return The appointment's ID.
     */
    public String getAppointmentId() {
        return this.appointmentId;
    }

    /**
     * Gets the type of service provided during the appointment.
     *
     * @return The appointment service type.
     */
    public AppointmentServiceType getServiceType() {
        return this.serviceType;
    }

    /**
     * Gets the consultation notes from the appointment, describing the outcome.
     *
     * @return The consultation notes.
     */
    public String getConsultationNotes() {
        return this.consultationNotes;
    }

    /**
     * Returns a CSV representation of the appointment outcome data, with commas in consultation notes escaped.
     *
     * @return A CSV string containing appointment outcome data.
     */
    @Override
    public String toCsvString() {
        // Escape any commas in consultationNotes to prevent CSV parsing issues
        String escapedNotes = consultationNotes.replace(",", ";");
        
        return String.join(",",
            id,                         // from BaseEntity
            patientId,
            appointmentId,
            serviceType.toString(),     // enum to string
            escapedNotes,
            createdAt.toString(),       // from BaseEntity
            updatedAt.toString()        // from BaseEntity
        );
    }
}
