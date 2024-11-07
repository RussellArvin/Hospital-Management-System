package repository;


import model.Appointment;
import repository.base.CsvRepository;
import repository.mapper.AppointmentMapper;

public class AppointmentRepository extends CsvRepository<Appointment, AppointmentMapper> {
    private static final String CSV_FILE = "data/appointmentts.csv";
    private static final String CSV_HEADER = "id,doctorId,patientId,startDateTime,endDateTime,status,cancelReason,createdAt,updatedAt";

    public AppointmentRepository(){
        super(CSV_FILE, CSV_HEADER, new AppointmentMapper());
    }

    @Override
    public Appointment[] findAll() {
        return super.findAll(Appointment.class);
    }
}
