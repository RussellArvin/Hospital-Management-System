package model;

import enums.AppointmentServiceType;
import java.time.LocalDateTime;

/**
 * The AppointmentOutcomeDetail class extends AppointmentOutcome, providing detailed information
 * about an appointment outcome, including the associated appointment and any prescribed medications.
 * 
 * @author Russel Arvin 
 * @version 1.0 
 */
public class AppointmentOutcomeDetail extends AppointmentOutcome {

    private AppointmentDetail appointment;
    private PrescriptionWithMedicine[] prescriptions;

    /**
     * Constructs an AppointmentOutcomeDetail with the specified details, including prescriptions and appointment details.
     *
     * @param id               The unique ID of the appointment outcome.
     * @param patientId        The ID of the patient associated with this outcome.
     * @param appointmentId    The ID of the appointment associated with this outcome.
     * @param serviceType      The type of service provided during the appointment.
     * @param consultationNotes Notes from the consultation, describing the outcome.
     * @param createdAt        The date and time when this outcome was created.
     * @param updatedAt        The date and time when this outcome was last updated.
     * @param prescriptions    An array of prescriptions issued during the appointment.
     * @param appointment      The detailed appointment associated with this outcome.
     */
    public AppointmentOutcomeDetail(
        String id,
        String patientId,
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PrescriptionWithMedicine[] prescriptions,
        AppointmentDetail appointment
    ) {
        super(id, patientId, appointmentId, serviceType, consultationNotes, createdAt, updatedAt);
        this.prescriptions = prescriptions;
        this.appointment = appointment;
    }

    /**
     * Creates an AppointmentOutcomeDetail object from an existing AppointmentOutcome, 
     * associating it with detailed appointment and prescription information.
     *
     * @param outcome       The existing AppointmentOutcome object to use as a base.
     * @param prescriptions An array of prescriptions associated with the appointment outcome.
     * @param appointment   The detailed appointment associated with this outcome.
     * @return A new AppointmentOutcomeDetail object with prescriptions and appointment details.
     */
    public static AppointmentOutcomeDetail fromAppointmentOutcome(
        AppointmentOutcome outcome,
        PrescriptionWithMedicine[] prescriptions,
        AppointmentDetail appointment
    ){
        return new AppointmentOutcomeDetail(
            outcome.getId(),
            outcome.getPatientId(),
            outcome.getAppointmentId(),
            outcome.getServiceType(),
            outcome.getConsultationNotes(),
            outcome.getCreatedAt(),
            outcome.getUpdatedAt(),
            prescriptions,
            appointment   
        );
    }

    /**
     * Gets the array of prescriptions associated with the appointment outcome.
     *
     * @return An array of PrescriptionWithMedicine objects.
     */
    public PrescriptionWithMedicine[] getPrescriptions() {
        return this.prescriptions;
    }

    /**
     * Gets the detailed appointment associated with this appointment outcome.
     *
     * @return The AppointmentDetail object associated with this outcome.
     */
    public AppointmentDetail getAppointment() {
        return this.appointment;
    }
}
