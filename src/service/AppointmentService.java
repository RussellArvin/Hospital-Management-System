package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import enums.AppointmentStatus;
import model.Appointment;
import model.AppointmentDetail;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    
    public AppointmentService(
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository
    ){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public String getDoctorId(String name){
        try{
            Doctor doctor = doctorRepository.findOneByName(name);
            if(doctor == null) return null;
            return doctor.getId();
        } catch(Exception e){
            System.out.println("Something went wrong when finding the doctor");
            return null;
        }
    }

    public String rescheduleAppointment(
        String appointmentId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ){
        try{
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if(appointment == null) return "Unable to find appointment";

            appointment.setStartDateTime(startDateTime);
            appointment.setEndDateTime(endDateTime);
            appointmentRepository.update(appointment);
            return null;
        }catch(Exception e){
            return "Something went wrong when rescheduling appointment";
        }
    }

    public String createAppointment(
        String patientId,
        String doctorId,
        LocalDateTime startDateTime,
        LocalDateTime enDateTime
    ){
        try{
            Doctor doctor = doctorRepository.findOne(doctorId);
            if(doctor == null){
                return "Doctor could not be found";
            }

            Patient patient = patientRepository.findOne(patientId);
            if(patient == null){
                return "Patient could not be found";
            }

            Appointment appointment = new Appointment(patientId, doctorId, startDateTime, enDateTime);
            appointmentRepository.save(appointment);
            return null;
        } catch(Exception e){
            return "Something went wrong when creating an appointment";
        }
    }

    public String setStatus(
        String appointmentId,
        AppointmentStatus status,
        String cancelReason
    ) {
        try{
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if(appointment == null) {
                return "Cant find appointment to update";
            }
    
            appointment.setStatus(status);
            if(status == AppointmentStatus.DOCTOR_CANCELLED) appointment.setCancelReason(cancelReason);
            appointmentRepository.update(appointment);
            return null;
        }catch(Exception e){
            return "Something went wrong when updating the appointment";
        }
    }

    public AppointmentDetail[] findPendingApprovedByPatient(String patientId){
        AppointmentDetail[] appointments = this.findByPatient(patientId);

        return Arrays.stream(appointments)
        .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED || 
                             appointment.getStatus() == AppointmentStatus.CONFIRMED)
        .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findApprovedByPatient(String patientId){
        AppointmentDetail[] appointments = this.findByPatient(patientId);

        return Arrays.stream(appointments)
        .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED)
        .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findRequestedByDoctor(String doctorId) {
        AppointmentDetail[] appointments = this.findByDoctor(doctorId);
        
        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED)
            .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findByDoctor(String doctorId){
        Doctor doctor = doctorRepository.findOne(doctorId);

        Appointment[] appointments = appointmentRepository.findManyByDoctorId(doctorId);
        AppointmentDetail[] details = new AppointmentDetail[appointments.length];

        for(int i = 0; i < appointments.length; i++){
            Appointment appointment = appointments[i];
            Patient patient = patientRepository.findOne(appointment.getPatientId());

            //Create object from static factory method
            details[i] = AppointmentDetail.fromAppointmentDetail(
                appointment, 
                doctor, 
                patient
            );

        }

        return details;

    }

    public AppointmentDetail[] getDoctorAppointmentsByDate(String doctorId, LocalDate date){
        AppointmentDetail[] appointments = this.findByDoctor(doctorId);

        return Arrays.stream(appointments)
            .filter(appointment -> 
                (appointment.getStatus() == AppointmentStatus.REQUESTED || 
                appointment.getStatus() == AppointmentStatus.CONFIRMED) &&
                appointment.getStartDateTime().toLocalDate().equals(date)
            )
            .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findAll(){
        Appointment[] appointments = appointmentRepository.findAll();
        AppointmentDetail[] details = new AppointmentDetail[appointments.length];

        for(int i = 0; i < appointments.length; i++){
            Appointment appointment = appointments[i];
            Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());
            Patient patient = patientRepository.findOne(appointment.getPatientId());

            //Create object from static factory method
            details[i] = AppointmentDetail.fromAppointmentDetail(
                appointment, 
                doctor, 
                patient
            );

        }

        return details;
    }

    public AppointmentDetail[] findByPatient(String patientId){
        Patient patient = patientRepository.findOne(patientId);

        Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);
        AppointmentDetail[] details = new AppointmentDetail[appointments.length];

        for(int i = 0; i < appointments.length; i++){
            Appointment appointment = appointments[i];
            Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());

            //Create object from static factory method
            details[i] = AppointmentDetail.fromAppointmentDetail(
                appointment, 
                doctor, 
                patient
            );

        }

        return details;
    }
}
