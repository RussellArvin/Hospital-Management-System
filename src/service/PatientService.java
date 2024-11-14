package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;

import enums.AppointmentStatus;
import enums.BloodType;
import enums.Gender;
import enums.MedicalRecordType;
import model.Appointment;
import model.BaseEntity;
import model.Doctor;
import model.MedicalRecord;
import model.MedicalRecordDetail;
import model.Patient;
import model.PatientVital;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.MedicalRecordRepository;
import repository.PatientRepository;
import repository.PatientVitalRepository;
import util.Constant;
import util.PasswordUtil;

public class PatientService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private MedicalRecordRepository medicalRecordRepository;
    private PatientVitalRepository patientVitalRepository;

    public PatientService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        MedicalRecordRepository medicalRecordRepository,
        PatientVitalRepository patientVitalRepository
    ){
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientVitalRepository = patientVitalRepository;
    }

    public String create(
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        BloodType bloodType,
        int phoneNumber,
        String email
    ){
        try{
            Patient patient = patientRepository.findLatest();
            String id = generateNextPatientId(patient.getId());
            byte[] salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(Constant.DEFAULT_PASSWORD, salt);
            Patient newPatient = new Patient(
                id, 
                hashedPassword, 
                salt, 
                name, 
                Period.between(dateOfBirth, LocalDate.now()).getYears(), 
                dateOfBirth, 
                gender, 
                bloodType, 
                phoneNumber, 
                email
            );
            patientRepository.save(newPatient);
            System.out.println("Your userID is: " + id);
            return null;
        } catch(Exception e){
            return "Something went wrong when creating the patient";
        }
    }

    private String generateNextPatientId(String currentId) {
        // If no current ID exists, start with P001
        if (currentId == null || currentId.isEmpty()) {
            return "P001";
        }
        
        // Extract the numeric part
        String numStr = currentId.substring(1); // Remove 'P'
        
        try {
            // Parse the number and add 1
            int nextNum = Integer.parseInt(numStr) + 1;
            
            // Format back to 3 digits with leading zeros
            return String.format("P%03d", nextNum);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid patient ID format: " + currentId);
        }
    }

    public Patient[] findAll(){
        return this.patientRepository.findAll();
    }

    public LocalDateTime lastAppointmentWithDoctor(
            String doctorId, 
            String patientId
        ){

            Patient patient = patientRepository.findOne(patientId);
            if(patient == null) return null;

            Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);

            if(doctorId != null){
                Doctor doctor = doctorRepository.findOne(doctorId);
                if(doctor == null) return null;
                return Arrays.stream(appointments)
                    .filter(appointment -> 
                        appointment.getDoctorId().equals(doctorId) && 
                        appointment.getStatus() == AppointmentStatus.COMPLETED
                    )
                    .map(Appointment::getStartDateTime)
                    .sorted(Comparator.reverseOrder())  
                    .findFirst()                        
                    .orElse(null);      
            }

            return Arrays.stream(appointments)
            .filter(appointment -> 
                appointment.getStatus() == AppointmentStatus.COMPLETED
            )
            .map(Appointment::getStartDateTime)
            .sorted(Comparator.reverseOrder())  
            .findFirst()                        
            .orElse(null);      
            
                        
    }

    public MedicalRecordDetail[] getMedicalRecordsByPatientId(String patientId){
        Patient patient = patientRepository.findOne(patientId);
        if(patient == null) return null;

        MedicalRecord[] rawRecords =  medicalRecordRepository.findManyByPatientId(patientId);
        MedicalRecordDetail[] records = new MedicalRecordDetail[rawRecords.length];

        for(int i = 0; i < records.length; i ++){
            MedicalRecord rawRecord = rawRecords[i];
            Doctor doctor = doctorRepository.findOne(rawRecord.getDoctorId());

            records[i] = MedicalRecordDetail.fromMedicalRecord(rawRecord, doctor);
        }
        return records;
    }

    public PatientVital geLatestPatientVitalByPatientId(String patientId){
        PatientVital[] vitals =  this.patientVitalRepository.findManyByPatientId(patientId);

        // Return null if no vitals found
        if (vitals == null || vitals.length == 0) {
            return null;
        }

        return Arrays.stream(vitals)
                .max((v1, v2) -> v1.getCreatedAt().compareTo(v2.getCreatedAt()))
                .orElse(null);
    }

    public String createMedicalRecord(
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details
    ) {
        try{
            Patient patient = patientRepository.findOne(patientId);
            if(patient == null) return "Unable to find patient";

            MedicalRecord record = new MedicalRecord(patientId, doctorId, type, details);
            medicalRecordRepository.save(record);
            return null;
        } catch(Exception e){
            return "Something went wrong when creating Medical Record";
        }
    }

    public Patient getPatientByName(
        String patientName
    ){
        Patient patient = patientRepository.findOneByName(patientName);

        return patient;
    }

    public String createPatientVital(
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure
    ) {
        try{
            Patient patient = patientRepository.findOne(patientId);
            if(patient == null) return "Unable to find patient";

            PatientVital vital = new PatientVital(patientId, bloodOxygen, height, weight, bloodPressure);
            patientVitalRepository.save(vital);
            return null;
        } catch(Exception e){
            return "Something went wrong when creating patient vital";
        }
    }
}
