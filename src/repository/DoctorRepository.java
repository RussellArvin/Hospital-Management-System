package repository;

import model.Doctor;
import repository.base.CsvRepository;
import repository.mapper.DoctorMapper;

public class DoctorRepository extends CsvRepository<Doctor, DoctorMapper> {
    private static final String CSV_FILE = "data/doctors.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,startWorkHours,endWorkHours,createdAt,updatedAt";

    public DoctorRepository(){
        super(CSV_FILE,CSV_HEADER, new DoctorMapper());
    }

    @Override
    public Doctor[] findAll() {
        return super.findAll(Doctor.class);
    }
}
