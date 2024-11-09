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

    public LocalDateTime getStartDateTime(){
        return this.startDateTime;
    }

    public void setStartDateTime(LocalDateTime date){
        this.startDateTime = date;
    }

    public LocalDateTime getEndDateTime(){
        return this.endDateTime;
    }

    public void setEndDateTime(LocalDateTime date){
        this.endDateTime = date;
    }

    public AppointmentStatus getStatus(){
        return this.status;
    }

    public void setStatus(AppointmentStatus status){
        this.status = status;
    }

    public void setCancelReason(String cancelReason){
        this.cancelReason = cancelReason;
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