package model;

import java.time.LocalDateTime;

import enums.MedicalRecordType;

public class MedicalRecord extends BaseEntity {
    private String patientId;
    private String doctorId;
    private MedicalRecordType type;
    private String details;

    public MedicalRecord(
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details
    ) {
        super();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.details = details;
    }

    public MedicalRecord(
        String id,
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.details = details;
    }

    public MedicalRecordType getType(){
        return this.type;
    }

    public String getDetails(){
        return this.details;
    }

    public String getPatientId(){
        return this.patientId;
    }

    public String toCsvString(){
        return String.join(",",
            id,
            patientId,
            doctorId,
            type.toString(),
            details,
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
