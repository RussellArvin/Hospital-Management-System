package service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import enums.AppointmentStatus;
import model.Appointment;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

public class PatientService {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;

    public PatientService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository
    ){
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
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
}
