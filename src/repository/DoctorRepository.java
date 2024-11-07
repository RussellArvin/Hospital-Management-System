package repository;

import model.Doctor;
import repository.base.CsvRepository;
import repository.mapper.DoctorMapper;

public class DoctorRepository extends CsvRepository<Doctor> {
    private static final String CSV_FILE = "data/doctors.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,startWorkHours,endWorkHours,createdAt,updatedAt";

    public DoctorRepository(){
        super(CSV_FILE,CSV_HEADER);
    }

    public Doctor findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return DoctorMapper.fromCsvString(line);
    }
}
