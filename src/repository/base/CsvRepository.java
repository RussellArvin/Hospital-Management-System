package repository.base;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.BaseEntity;
import repository.mapper.BaseMapper;

/**
 * The CsvRepository class provides a generic repository for managing entities stored in a CSV file.
 * It supports basic CRUD operations (create, read, update, delete) for entities that extend BaseEntity
 * and are mapped using a specified mapper.
 *
 * @param <T> The type of entity managed by this repository, extending BaseEntity.
 * @param <M> The type of mapper used to convert between CSV data and entity instances.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public class CsvRepository<T extends BaseEntity, M extends BaseMapper<T>> {

    protected final CsvFileManager fileManager;
    protected final M mapper;

    /**
     * Constructs a CsvRepository with a specified file path, CSV header, and mapper.
     *
     * @param filePath The file path of the CSV file.
     * @param header   The CSV header for the file.
     * @param mapper   The mapper for converting CSV data to entities and vice versa.
     */
    public CsvRepository(String filePath, String header, M mapper) {
        this.fileManager = new CsvFileManager(filePath, header);
        this.mapper = mapper;
    }

    /**
     * Saves a new entity to the CSV file. Throws an exception if an entity with the same ID already exists.
     *
     * @param entity The entity to save.
     */
    public void save(T entity) {
        T duplicateEntity = this.findOne(entity.getId());
        if(duplicateEntity != null) throw new RuntimeException("Item already exists for ID: " + entity.getId());

        this.fileManager.appendLine(entity.toCsvString());
    }

    /**
     * Saves an array of entities to the CSV file.
     *
     * @param entities The array of entities to save.
     */
    public void saveMany(T[] entities) {
        for (int i = 0; i < entities.length; i++) {
            this.save(entities[i]);
        }
    }

    /**
     * Finds and returns the latest entity in the CSV file.
     *
     * @return The latest entity.
     */
    public T findLatest() {
        String line = this.fileManager.getLastLine();
        return mapper.fromCsvString(line);
    }

    /**
     * Finds and returns an entity by its ID.
     *
     * @param id The ID of the entity to find.
     * @return The entity with the specified ID, or null if not found.
     */
    public T findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return mapper.fromCsvString(line);
    }

    /**
     * Finds and returns all entities from the CSV file as an array of type T.
     *
     * @param entityClass The class of the entity type.
     * @return An array of all entities.
     */
    protected T[] findAll(Class<T> entityClass) {
        List<String> lines = this.fileManager.readAllLines();
        return mapLines(lines, entityClass);
    }

    /**
     * Maps a list of CSV lines to an array of entities.
     *
     * @param lines       The list of CSV lines.
     * @param entityClass The class of the entity type.
     * @return An array of mapped entities.
     */
    protected T[] mapLines(List<String> lines, Class<T> entityClass) {
        List<T> entities = lines.stream()
            .filter(line -> !line.equals(this.fileManager.getHeader()))
            .map(mapper::fromCsvString)
            .collect(Collectors.toList());
        
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(entityClass, entities.size());
        return entities.toArray(array);
    }

    /**
     * Finds and returns all entities from the CSV file as an array of type T.
     *
     * @return An array containing all entities from the CSV file
     */
    public T[] findAll() {
        List<String> lines = this.fileManager.readAllLines();
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ((ParameterizedType)mapper.getClass().getGenericInterfaces()[0])
            .getActualTypeArguments()[0];
        return mapLines(lines, entityClass);
    }


    /**
     * Updates an existing entity in the CSV file. Throws an exception if the entity is not found.
     *
     * @param entity The entity to update.
     */
    public void update(T entity) {
        entity.setUpdatedAt();
    
        List<String> lines = this.fileManager.readAllLines();
        boolean found = false;
    
        List<String> updatedLines = new ArrayList<>();
        updatedLines.add(this.fileManager.getHeader());  // Add header
    
        // Update the specific entity line
        for (String line : lines) {
            if (line.startsWith(this.fileManager.getHeader())) continue;
    
            String id = line.split(",")[0];
    
            if (id.equals(entity.getId())) {
                updatedLines.add(entity.toCsvString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
    
        if (!found) {
            throw new RuntimeException("Item not found for update: " + entity.getId());
        }
    
        this.fileManager.writeAllLines(updatedLines);
    }

    /**
     * Deletes an entity from the CSV file. Throws an exception if the entity is not found.
     *
     * @param entity The entity to delete.
     */
    public void delete(T entity) {
        String id = entity.getId();
        T entityExists = findOne(id);

        if(entityExists == null) {
            throw new RuntimeException("Item not found for deletion: " + id);
        }
        
        try {
            this.fileManager.deleteLine(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete item: " + e.getMessage());
        }
    }
}
