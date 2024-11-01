package model;

import enums.AppointmentStatus;
import java.time.LocalDateTime;

public class Appointment extends BaseEntity{
    private String doctorId;
    private String patientId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private String cancelReason;
    

    public Appointment(
        String patientId,
        String doctorId,
        LocalDateTime dateTime
    ) {
        super(BaseEntity.generateUUID());

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = AppointmentStatus.REQUESTED;
        this.cancelReason = null;
    }

    public void confirm(){
        this.status = AppointmentStatus.CONFIRMED;
    }

    public void complete(){
        this.status = AppointmentStatus.COMPLETED;
    }

    public void cancelByDoctor(String cancelReason){
        this.status = AppointmentStatus.DOCTOR_CANCELLED;
        this.cancelReason = cancelReason;
    }

    public void cancelByPatient(String cancelReason){
        this.status = AppointmentStatus.PATIENT_CANCELLED;
        this.cancelReason = cancelReason;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            id,                         // from BaseEntity
            patientId,
            doctorId,
            dateTime.toString(),        // LocalDateTime to string
            status.toString(),          // enum to string
            cancelReason != null ? cancelReason.replace(",", ";") : "",  // handle null and escape commas
            createdAt.toString(),       // from BaseEntity
            updatedAt.toString()        // from BaseEntity
        );
    }

}