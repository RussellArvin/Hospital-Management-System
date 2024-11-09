package model;

import java.time.LocalDateTime;

import enums.AppointmentServiceType;

public class AppointmentOutcome extends BaseEntity {
    private String patientId;
    private String appointmentId;
    private AppointmentServiceType serviceType;
    private String consultationNotes;
    

    public AppointmentOutcome(
       String patientId,
       String appointmentId,
       AppointmentServiceType serviceType,
       String consultationNotes
    ) {
        super();
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.consultationNotes = consultationNotes;
    }

    public AppointmentOutcome(
        String id,
        String patientId,
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id,createdAt, updatedAt);
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.consultationNotes = consultationNotes;
    }

    public String getPatientId(){
        return this.patientId;
    }

    public String getAppointmentId(){
        return this.appointmentId;
    }

    public AppointmentServiceType getServiceType(){
        return this.serviceType;
    }

    public String getConsultationNotes(){
        return this.consultationNotes;
    }

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