package repository;

import model.Pharmacist;
import repository.base.CsvRepository;
import repository.mapper.PharmacistMapper;

/**
 * The PharmacistRepository class extends CsvRepository for managing Pharmacist entities.
 * It specifies the CSV file and header used for Pharmacist data and provides methods
 * for accessing and managing Pharmacist records.
 * 
 * @author Tan Jou Yuan
 * @version 1.0
 */
public class PharmacistRepository extends CsvRepository<Pharmacist, PharmacistMapper> {

    private static final String CSV_FILE = "data/pharmacists.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,createdAt,updatedAt";

    /**
     * Constructs a PharmacistRepository with predefined CSV file and header information
     * for storing Pharmacist data and initializes the PharmacistMapper.
     */
    public PharmacistRepository() {
        super(CSV_FILE, CSV_HEADER, new PharmacistMapper());
    }
}
