package model;

import java.time.LocalDateTime;

import enums.MedicalRecordType;

public class MedicalRecordDetail extends MedicalRecord {
    private Doctor doctor;

    public MedicalRecordDetail(
        String id,
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Doctor doctor
    ) {
        super(id, patientId, doctorId, type, details, createdAt, updatedAt);
        this.doctor = doctor;
    }

    public Doctor getDoctor(){
        return this.doctor;
    }

    public static MedicalRecordDetail fromMedicalRecord(
        MedicalRecord record,
        Doctor doctor
    ) {
        return new MedicalRecordDetail(
            record.getId(),
            record.getPatientId(),
            record.getDoctorId(),
            record.getType(),
            record.getDetails(),
            record.getCreatedAt(),
            record.getUpdatedAt(),
            doctor    
        );
    }
}
