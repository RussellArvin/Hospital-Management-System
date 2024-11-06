package repository;

import model.AppointmentOutcome;
import repository.base.CsvRepository;
import repository.mapper.AppointmentOutcomeMapper;

public class AppointmentOutcomeRepository extends CsvRepository<AppointmentOutcome> {
    private static final String CSV_FILE = "data/appointment-outcomes.csv";
    private static final String CSV_HEADER = "id,appointmentId,serviceType,consultationNotes,createdAt,updatedAt";

    public AppointmentOutcomeRepository(){
        super(CSV_FILE,CSV_HEADER);
    }

    public AppointmentOutcome findOne(String id){
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return AppointmentOutcomeMapper.fromCsvString(line);
    }

    public AppointmentOutcome findOneByAppointmentId(String appointmentId){
        String line = this.fileManager.findLineByColumnValue("appointmentId", appointmentId);
        return AppointmentOutcomeMapper.fromCsvString(line);
    }
}
