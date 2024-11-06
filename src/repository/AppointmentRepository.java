package repository;


import model.Appointment;
import repository.base.CsvRepository;
import repository.mapper.AppointmentMapper;

public class AppointmentRepository extends CsvRepository<Appointment> {
    private static final String CSV_FILE = "data/appointmentts.csv";
    private static final String CSV_HEADER = "id,doctorId,patientId,startDateTime,endDateTime,status,cancelReason,createdAt,updatedAt";

    public AppointmentRepository(){
        super(CSV_FILE, CSV_HEADER);
    }

    public Appointment findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return AppointmentMapper.fromCsvString(line);
    }
}
