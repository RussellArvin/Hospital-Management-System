package repository;

import model.Doctor;
import repository.base.CsvRepository;
import repository.mapper.DoctorMapper;

/**
 * The DoctorRepository class extends CsvRepository for managing Doctor entities.
 * It specifies the CSV file and header used for Doctor data and provides methods
 * for accessing and managing Doctor records based on their name.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class DoctorRepository extends CsvRepository<Doctor, DoctorMapper> {

    private static final String CSV_FILE = "data/doctors.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,startWorkHours,endWorkHours,createdAt,updatedAt";

    /**
     * Constructs a DoctorRepository with predefined CSV file and header information
     * for storing Doctor data and initializes the DoctorMapper.
     */
    public DoctorRepository() {
        super(CSV_FILE, CSV_HEADER, new DoctorMapper());
    }

    /**
     * Retrieves all Doctor records from the CSV file.
     *
     * @return An array of all Doctor objects in the repository.
     */
    @Override
    public Doctor[] findAll() {
        return super.findAll(Doctor.class);
    }

    /**
     * Finds and returns a Doctor record by the specified name.
     *
     * @param name The name of the doctor to search for.
     * @return The Doctor object with the specified name, or null if not found.
     */
    public Doctor findOneByName(String name) {
        String line = this.fileManager.findLineByColumnValue("name", name);
        
        if(line == null) return null;
        return mapper.fromCsvString(line);
    }
}
