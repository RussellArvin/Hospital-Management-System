package model;

import model.BaseEntity;
import enums.AppointmentServiceType;

public class AppointmentOutcome extends BaseEntity {
    private String appointmentId;
    private AppointmentServiceType serviceType;
    private String consultationNotes;
    

    public AppointmentOutcome(
       String appointmentId,
       AppointmentServiceType serviceType,
       String consultationNotes
    ) {
        super(BaseEntity.generateUUID());
        this.appointmentId = appointmentId;
        this.serviceType = serviceType;
        this.consultationNotes = consultationNotes;
    }

        @Override
    public String toCsvString() {
        // Escape any commas in consultationNotes to prevent CSV parsing issues
        String escapedNotes = consultationNotes.replace(",", ";");
        
        return String.join(",",
            id,                         // from BaseEntity
            appointmentId,
            serviceType.toString(),     // enum to string
            escapedNotes,
            createdAt.toString(),       // from BaseEntity
            updatedAt.toString()        // from BaseEntity
        );
    }
}