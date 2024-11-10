package model;

import java.time.LocalDateTime;

import enums.AppointmentServiceType;

public class AppointmentOutcomeDetail extends AppointmentOutcome {
    private AppointmentDetail appointment;
    private PrescriptionWithMedicine[] prescriptions;

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

    public PrescriptionWithMedicine[] getPrescriptions(){
        return this.prescriptions;
    }

    public AppointmentDetail getAppointment(){
        return this.appointment;
    }
}
