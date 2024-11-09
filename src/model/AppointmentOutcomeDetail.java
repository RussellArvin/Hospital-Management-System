package model;

import java.time.LocalDateTime;

import enums.AppointmentServiceType;

public class AppointmentOutcomeDetail extends AppointmentOutcome {
    Prescription[] prescriptions;

    public AppointmentOutcomeDetail(
        String id,
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Prescription[] prescriptions
    ) {
        super(id, appointmentId, serviceType, consultationNotes, createdAt, updatedAt);
        this.prescriptions = prescriptions;
    }

    public static AppointmentOutcomeDetail fromAppointmentOutcome(
        AppointmentOutcome outcome,
        Prescription[] prescriptions
    ){
        return new AppointmentOutcomeDetail(
            outcome.getId(),
            outcome.getAppointmentId(),
            outcome.getServiceType(),
            outcome.getConsultationNotes(),
            outcome.getCreatedAt(),
            outcome.getUpdatedAt(),
            prescriptions   
        );
    }
}
