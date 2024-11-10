package repository;

import java.util.List;

import model.MedicalRecord;
import repository.base.CsvRepository;
import repository.mapper.MedicalRecordMapper;

public class MedicalRecordRepository extends CsvRepository<MedicalRecord,MedicalRecordMapper> {
    private static final String CSV_FILE = "data/medical-record.csv";
    private static final String CSV_HEADER = "id,patientId,doctorId,type,details,createdAt,updatedAt";

    public MedicalRecordRepository(){
        super(CSV_FILE,CSV_HEADER, new MedicalRecordMapper());
    }

    @Override
    public MedicalRecord[] findAll(){
        return super.findAll(MedicalRecord.class);
    }

    public MedicalRecord[] findManyByPatientId(String patientId){
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId",patientId);
        return super.mapLines(lines, MedicalRecord.class);
    }
}
