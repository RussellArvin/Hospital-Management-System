package model;

import java.time.LocalDateTime;

public class PatientVital extends BaseEntity {
    private String patientId;
    private int bloodOxygen;
    private int height;
    private int weight;
    private int bloodPressure;

    public PatientVital(
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure
    ){
        super();
        this.patientId = patientId;
        this.bloodOxygen = bloodOxygen;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
    }

    public PatientVital(
        String id,
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(patientId, createdAt, updatedAt);
        this.patientId = patientId;
        this.bloodOxygen = bloodOxygen;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
    }

    public String getPatientId(){
        return this.patientId;
    }

    public int getBloodOxygen(){
        return this.bloodOxygen;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWeight(){
        return this.weight;
    }

    public int getBloodPressure(){
        return this.bloodPressure;
    }

    public String toCsvString(){
        return String.join(",",
            id,
            patientId,
            String.valueOf(bloodOxygen),
            String.valueOf(height),
            String.valueOf(weight),
            String.valueOf(bloodPressure),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
