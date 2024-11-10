package service;

import model.AppointmentDetail;
import model.AppointmentOutcome;
import model.AppointmentOutcomeDetail;

public class PatientService {
    

    // public AppointmentOutcomeDetail[] findByDoctorId(String doctorId){
    //     AppointmentDetail[] appointments = appointmentService.findCompletedByDoctor(doctorId);
        
    //     AppointmentOutcomeDetail[] outcomes  = new AppointmentOutcomeDetail[appointments.length];
    //     for(int i = 0; i < outcomes.length; i++){
    //         String appointmentId = appointments[i].getId();
    //         AppointmentOutcome rawOutcome = appointmentOutcomeRepository.findOneByAppointmentId(appointmentId);
    //         outcomes[i] = mapDetail(rawOutcome);
    //     }

    //     return outcomes;
    // }
}
