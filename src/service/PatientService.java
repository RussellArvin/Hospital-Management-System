package service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import enums.AppointmentStatus;
import enums.MedicalRecordType;
import model.Appointment;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.MedicalRecordRepository;
import repository.PatientRepository;

public class PatientService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private MedicalRecordRepository medicalRecordRepository;

    public PatientService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        MedicalRecordRepository medicalRecordRepository
    ){
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public LocalDateTime lastAppointmentWithDoctor(
            String doctorId, 
            String patientId
        ){
            Doctor doctor = doctorRepository.findOne(doctorId);
            Patient patient = patientRepository.findOne(patientId);
            if(patient == null || doctor == null) return null;

            Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);
            
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

    public MedicalRecord[] getMedicalRecordsByPatientId(String patientId){
        Patient patient = patientRepository.findOne(patientId);
        if(patient == null) return null;

        return medicalRecordRepository.findManyByPatientId(patientId);
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
}
