package service;

import java.util.Arrays;
import java.util.Objects;

import enums.AppointmentStatus;
import model.Appointment;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

public class DoctorService {
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    public DoctorService(
        DoctorRepository doctorRepository,
        AppointmentRepository appointmentRepository,
        PatientRepository patientRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

public Patient[] getDoctorPatients(String doctorId) {
    Appointment[] appointments = appointmentRepository.findManyByDoctorId(doctorId);
    appointments = Arrays.stream(appointments)
        .filter(appointment -> 
            appointment.getStatus() != AppointmentStatus.DOCTOR_CANCELLED && 
            appointment.getStatus() != AppointmentStatus.PATIENT_CANCELLED
        )
        .toArray(Appointment[]::new);
    
    // Get unique patient IDs and fetch patient details
    return Arrays.stream(appointments)
        .map(Appointment::getPatientId)  
        .distinct()                      
        .map(patientRepository::findOne) 
        .filter(Objects::nonNull)        
        .toArray(Patient[]::new);     
}

    public String setAvailability(
        String doctorId,
        int startWorkHours,
        int endWorkHours
    ){
        try{
            Doctor doctor = doctorRepository.findOne(doctorId);
            if(doctor == null) return "Unable to find doctor";

            doctor.setWorkHours(startWorkHours, endWorkHours);
            doctorRepository.update(doctor);
            return null;
        } catch(Exception e){
            return "Something went wrong when setting doctor's availability";
        }
    }
}
