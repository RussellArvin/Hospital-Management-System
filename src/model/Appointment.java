package model;

import enums.AppointmentStatus;
import java.time.LocalDateTime;

public class Appointment extends BaseEntity{
    private String doctorId;
    private String patientId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus status;
    private String cancelReason;
    

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

    public LocalDateTime getStartDateTime(){
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime(){
        return this.endDateTime;
    }

    public AppointmentStatus getStatus(){
        return this.status;
    }

    public void setStatus(AppointmentStatus status){
        this.status = status;
    }

    public String getPatientId(){
        return this.patientId;
    }

    public String getDoctorId(){
        return this.doctorId;
    }

    public String getCancelReason(){
        return this.cancelReason;
    }

    @Override
    public String toCsvString() {
        return String.join(",",
            id,
            doctorId, 
            patientId,
            startDateTime.toString(),
            endDateTime.toString(),
            status.toString(),          // enum to string
            cancelReason != null ? cancelReason.replace(",", ";") : "",  // handle null and escape commas
            createdAt.toString(),       // from BaseEntity
            updatedAt.toString()        // from BaseEntity
        );
    }

}