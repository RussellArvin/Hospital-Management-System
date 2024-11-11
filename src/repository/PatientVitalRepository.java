package repository;

import java.util.List;

import model.PatientVital;
import repository.base.CsvRepository;
import repository.mapper.PatientVitalMapper;

public class PatientVitalRepository extends CsvRepository<PatientVital, PatientVitalMapper> {
        private static final String CSV_FILE = "data/patient-vitals.csv";
    private static final String CSV_HEADER = "id,patientId,bloodOxygen,height,weight,bloodPressure,createdAt,updatedAt";

    public PatientVitalRepository() {
        super(CSV_FILE, CSV_HEADER, new PatientVitalMapper());
    }

    @Override
    public PatientVital[] findAll() {
        return super.findAll(PatientVital.class);
    }

        public PatientVital[] findManyByPatientId(String patientId){
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId",patientId);
        return super.mapLines(lines, PatientVital.class);
    }
}
