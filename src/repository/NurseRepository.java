package repository;

import model.Nurse;
import repository.base.CsvRepository;
import repository.mapper.NurseMapper;

/**
 * The NurseRepository class extends CsvRepository for managing Nurse entities.
 * It specifies the CSV file and header used for Nurse data and provides methods
 * for accessing and managing Nurse records.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class NurseRepository extends CsvRepository<Nurse, NurseMapper> {

    private static final String CSV_FILE = "data/nurses.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,createdAt,updatedAt";

    /**
     * Constructs a NurseRepository with predefined CSV file and header information
     * for storing Nurse data and initializes the NurseMapper.
     */
    public NurseRepository() {
        super(CSV_FILE, CSV_HEADER, new NurseMapper());
    }

    /**
     * Retrieves all Nurse records from the CSV file.
     *
     * @return An array of all Nurse objects in the repository.
     */
    @Override
    public Nurse[] findAll() {
        return super.findAll(Nurse.class);
    }
}
