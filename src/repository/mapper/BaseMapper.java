package repository.mapper;

import model.BaseEntity;

/**
 * The BaseMapper interface defines the contract for mappers that convert CSV strings to entity objects.
 * It is designed for entities that extend BaseEntity and provides a method for parsing CSV data.
 *
 * @param <T> The type of entity that the mapper will handle, extending BaseEntity.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public interface BaseMapper<T extends BaseEntity> {

    /**
     * Converts a CSV string into an entity object of type T.
     *
     * @param csvString The CSV string representing the entity.
     * @return The entity object constructed from the CSV data.
     */
    T fromCsvString(String csvString);
}
