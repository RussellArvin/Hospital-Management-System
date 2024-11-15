package repository;

import model.Administrator;
import repository.base.CsvRepository;
import repository.mapper.AdministratorMapper;

/**
 * The AdministratorRepository class extends CsvRepository for managing Administrator entities.
 * It specifies the CSV file and header used for Administrator data and provides methods
 * for accessing and managing Administrator records.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class AdministratorRepository extends CsvRepository<Administrator, AdministratorMapper> {

    private static final String CSV_FILE = "data/administrators.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gendercreatedAt,updatedAt";

    /**
     * Constructs an AdministratorRepository with predefined CSV file and header information
     * for storing Administrator data and initializes the AdministratorMapper.
     */
    public AdministratorRepository() {
        super(CSV_FILE, CSV_HEADER, new AdministratorMapper());
    }

    /**
     * Retrieves all Administrator records from the CSV file.
     *
     * @return An array of all Administrator objects in the repository.
     */
    @Override
    public Administrator[] findAll() {
        return super.findAll(Administrator.class);
    }
}
