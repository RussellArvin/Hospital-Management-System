package model;

import java.time.LocalDateTime;

import enums.AppointmentStatus;

public class AppointmentDetail extends Appointment {
    private Doctor doctor;
    private Patient patient;

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

    public Doctor getDoctor(){
        return this.doctor;
    }

    public Patient getPatient(){
        return this.patient;
    }
}
