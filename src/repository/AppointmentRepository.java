package repository;


import java.util.List;

import enums.AppointmentStatus;
import model.Appointment;
import repository.base.CsvRepository;
import repository.mapper.AppointmentMapper;

public class AppointmentRepository extends CsvRepository<Appointment, AppointmentMapper> {
    private static final String CSV_FILE = "data/appointments.csv";
    private static final String CSV_HEADER = "id,doctorId,patientId,startDateTime,endDateTime,status,cancelReason,createdAt,updatedAt";

    public AppointmentRepository(){
        super(CSV_FILE, CSV_HEADER, new AppointmentMapper());
    }

    @Override
    public Appointment[] findAll() {
        return super.findAll(Appointment.class);
    }


    public Appointment[] findManyByDoctorId(String doctorId){
        List<String> lines = this.fileManager.findLinesByColumnValue("doctorId",doctorId);
        return super.mapLines(lines, Appointment.class);
    }
    
    public Appointment[] findManyByPatientId(String patientId){
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId",patientId);
        return super.mapLines(lines, Appointment.class);
    }

    public Appointment[] findManyByStatus(AppointmentStatus status){
        List<String> lines = this.fileManager.findLinesByColumnValue("status",status.toString());
        return super.mapLines(lines, Appointment.class);
    }
}
