package model;

/**
 * The Entity interface defines the basic contract for entities in the system.
 * It includes methods for retrieving and setting an ID, as well as generating
 * a CSV representation of the entity.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0 
 */
public interface Entity {

    /**
     * Gets the unique ID of the entity.
     *
     * @return The entity's ID.
     */
    String getId();

    /**
     * Sets the unique ID of the entity.
     *
     * @param id The ID to set for the entity.
     */
    void setId(String id);

    /**
     * Returns a CSV string representation of the entity.
     *
     * @return A CSV-formatted string representing the entity.
     */
    String toCsvString();
}
