package service;

import enums.AppointmentStatus;
import enums.BloodType;
import enums.Gender;
import enums.MedicalRecordType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import model.Appointment;
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

/**
 * The PatientService class provides functionality for managing patient-related operations,
 * including patient creation, retrieving appointment details, medical records, and vitals.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class PatientService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private MedicalRecordRepository medicalRecordRepository;
    private PatientVitalRepository patientVitalRepository;

    /**
     * Constructs a PatientService with the required repositories.
     *
     * @param patientRepository         Repository for managing patient data.
     * @param appointmentRepository     Repository for managing appointment data.
     * @param doctorRepository          Repository for managing doctor data.
     * @param medicalRecordRepository   Repository for managing medical record data.
     * @param patientVitalRepository    Repository for managing patient vital data.
     */
    public PatientService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        MedicalRecordRepository medicalRecordRepository,
        PatientVitalRepository patientVitalRepository
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientVitalRepository = patientVitalRepository;
    }

    /**
     * Creates a new patient record.
     *
     * @param name         The name of the patient.
     * @param dateOfBirth  The birth date of the patient.
     * @param gender       The gender of the patient.
     * @param bloodType    The blood type of the patient.
     * @param phoneNumber  The phone number of the patient.
     * @param email        The email address of the patient.
     * @return null if successful, or an error message if patient creation fails.
     */
    public String create(
        String name,
        LocalDate dateOfBirth,
        Gender gender,
        BloodType bloodType,
        int phoneNumber,
        String email
    ) {
        try {
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
        } catch (Exception e) {
            return "Something went wrong when creating the patient";
        }
    }

    /**
     * Generates the next patient ID based on the current patient's ID.
     *
     * @param currentId The ID of the current patient.
     * @return The next patient ID in sequence.
     */
    private String generateNextPatientId(String currentId) {
        if (currentId == null || currentId.isEmpty()) {
            return "P001";
        }
        
        String numStr = currentId.substring(1);
        
        try {
            int nextNum = Integer.parseInt(numStr) + 1;
            return String.format("P%03d", nextNum);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid patient ID format: " + currentId);
        }
    }

    /**
     * Retrieves all patients.
     *
     * @return An array of all patients.
     */
    public Patient[] findAll() {
        return this.patientRepository.findAll();
    }

    /**
     * Retrieves the date and time of the last completed appointment between a patient and a doctor.
     *
     * @param doctorId  The ID of the doctor (optional).
     * @param patientId The ID of the patient.
     * @return The date and time of the last completed appointment or null if not found.
     */
    public LocalDateTime lastAppointmentWithDoctor(
            String doctorId, 
            String patientId
    ) {
        Patient patient = patientRepository.findOne(patientId);
        if (patient == null) return null;

        Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);

        if (doctorId != null) {
            Doctor doctor = doctorRepository.findOne(doctorId);
            if (doctor == null) return null;
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

    /**
     * Retrieves all medical records for a patient.
     *
     * @param patientId The ID of the patient.
     * @return An array of MedicalRecordDetail for the patient, or null if not found.
     */
    public MedicalRecordDetail[] getMedicalRecordsByPatientId(String patientId) {
        Patient patient = patientRepository.findOne(patientId);
        if (patient == null) return null;

        MedicalRecord[] rawRecords = medicalRecordRepository.findManyByPatientId(patientId);
        MedicalRecordDetail[] records = new MedicalRecordDetail[rawRecords.length];

        for (int i = 0; i < records.length; i++) {
            MedicalRecord rawRecord = rawRecords[i];
            Doctor doctor = doctorRepository.findOne(rawRecord.getDoctorId());

            records[i] = MedicalRecordDetail.fromMedicalRecord(rawRecord, doctor);
        }
        return records;
    }

    /**
     * Retrieves the latest patient vital data for a specific patient.
     *
     * @param patientId The ID of the patient.
     * @return The latest PatientVital data, or null if no data is available.
     */
    public PatientVital getLatestPatientVitalByPatientId(String patientId) {
        PatientVital[] vitals = this.patientVitalRepository.findManyByPatientId(patientId);

        if (vitals == null || vitals.length == 0) {
            return null;
        }

        return Arrays.stream(vitals)
                .max((v1, v2) -> v1.getCreatedAt().compareTo(v2.getCreatedAt()))
                .orElse(null);
    }

    /**
     * Creates a new medical record for a patient.
     *
     * @param patientId The ID of the patient.
     * @param doctorId  The ID of the doctor.
     * @param type      The type of medical record.
     * @param details   Additional details about the medical record.
     * @return null if successful, otherwise an error message.
     */
    public String createMedicalRecord(
        String patientId,
        String doctorId,
        MedicalRecordType type,
        String details
    ) {
        try {
            Patient patient = patientRepository.findOne(patientId);
            if (patient == null) return "Unable to find patient";

            MedicalRecord record = new MedicalRecord(patientId, doctorId, type, details);
            medicalRecordRepository.save(record);
            return null;
        } catch (Exception e) {
            return "Something went wrong when creating Medical Record";
        }
    }

    /**
     * Retrieves a patient by their name.
     *
     * @param patientName The name of the patient.
     * @return The Patient object if found, otherwise null.
     */
    public Patient getPatientByName(
        String patientName
    ) {
        Patient patient = patientRepository.findOneByName(patientName);
        return patient;
    }

    /**
     * Creates a new vital record for a patient.
     *
     * @param patientId     The ID of the patient.
     * @param bloodOxygen   The blood oxygen level of the patient.
     * @param height        The height of the patient.
     * @param weight        The weight of the patient.
     * @param bloodPressure The blood pressure of the patient.
     * @return null if successful, otherwise an error message.
     */
    public String createPatientVital(
        String patientId,
        int bloodOxygen,
        int height,
        int weight,
        int bloodPressure
    ) {
        try {
            Patient patient = patientRepository.findOne(patientId);
            if (patient == null) return "Unable to find patient";

            PatientVital vital = new PatientVital(patientId, bloodOxygen, height, weight, bloodPressure);
            patientVitalRepository.save(vital);
            return null;
        } catch (Exception e) {
            return "Something went wrong when creating patient vital";
        }
    }
}
