package repository;

import java.util.List;

import model.AppointmentOutcome;
import repository.base.CsvRepository;
import repository.mapper.AppointmentOutcomeMapper;

public class AppointmentOutcomeRepository extends CsvRepository<AppointmentOutcome,AppointmentOutcomeMapper> {
    private static final String CSV_FILE = "data/appointment-outcomes.csv";
    private static final String CSV_HEADER = "id,patientId,appointmentId,serviceType,consultationNotes,createdAt,updatedAt";

    public AppointmentOutcomeRepository(){
        super(CSV_FILE,CSV_HEADER,new AppointmentOutcomeMapper());
    }

    public AppointmentOutcome findOneByAppointmentId(String appointmentId){
        String line = this.fileManager.findLineByColumnValue("appointmentId", appointmentId);
        
        if(line == null) return null;
        return mapper.fromCsvString(line);
    }

    @Override
    public AppointmentOutcome[] findAll() {
        return super.findAll(AppointmentOutcome.class);
    }

    public AppointmentOutcome[] findManyByPatientId(String patientId){
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId",patientId);
        return super.mapLines(lines, AppointmentOutcome.class);
    }
}
