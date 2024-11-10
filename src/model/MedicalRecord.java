package model;

import java.time.LocalDateTime;

import enums.MedicalRecordType;

public class MedicalRecord extends BaseEntity {
    private MedicalRecordType type;
    private String details;

    public MedicalRecord(
        MedicalRecordType type,
        String details
    ) {
        super();
        this.type = type;
        this.details = details;
    }

    public MedicalRecord(
        String id,
        MedicalRecordType type,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.type = type;
        this.details = details;
    }

    public MedicalRecordType getType(){
        return this.type;
    }

    public String getDetails(){
        return this.details;
    }

    public String toCsvString(){
        return String.join(",",
            id,
            type.toString(),
            details,
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
