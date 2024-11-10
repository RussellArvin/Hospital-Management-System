package repository;

import model.MedicalRecord;
import repository.base.CsvRepository;
import repository.mapper.MedicalRecordMapper;

public class MedicalRecordRepository extends CsvRepository<MedicalRecord,MedicalRecordMapper> {
    private static final String CSV_FILE = "data/medical-record.csv";
    private static final String CSV_HEADER = "id,patientId,type,details,createdAt,updatedAt";

    public MedicalRecordRepository(){
        super(CSV_FILE,CSV_HEADER, new MedicalRecordMapper());
    }

    @Override
    public MedicalRecord[] findAll(){
        return super.findAll(MedicalRecord.class);
    }
}
