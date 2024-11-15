package model;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The BaseEntity class provides a base structure for entities in the system.
 * It includes common fields such as id, createdAt, and updatedAt, and defines
 * methods for ID management and timestamp handling.
 * 
 * @author Celeste Ho 
 * @version 1.0 
 */
public abstract class BaseEntity implements Entity {

    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    /**
     * Constructs a BaseEntity with a generated unique ID and initializes
     * createdAt and updatedAt timestamps to the current date and time.
     */
    protected BaseEntity(){
        this.id = BaseEntity.generateUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructs a BaseEntity with a specified ID and initializes createdAt and
     * updatedAt timestamps to the current date and time.
     *
     * @param id The unique ID of the entity.
     */
    protected BaseEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructs a BaseEntity with specified ID, createdAt, and updatedAt timestamps.
     *
     * @param id        The unique ID of the entity.
     * @param createdAt The date and time when the entity was created.
     * @param updatedAt The date and time when the entity was last updated.
     */
    protected BaseEntity(
        String id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the unique ID of the entity.
     *
     * @return The entity's ID.
     */
    @Override
    public String getId(){
        return id;
    }

    /**
     * Sets the unique ID of the entity.
     *
     * @param id The unique ID to set.
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Updates the updatedAt timestamp to the current date and time.
     */
    public void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Gets the creation timestamp of the entity.
     *
     * @return The createdAt timestamp.
     */
    public LocalDateTime getCreatedAt(){
        return this.createdAt;
    }

    /**
     * Gets the last updated timestamp of the entity.
     *
     * @return The updatedAt timestamp.
     */
    public LocalDateTime getUpdatedAt(){
        return this.updatedAt;
    }

    /**
     * Generates a new unique UUID as a string.
     *
     * @return A randomly generated unique identifier.
     */
    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Returns a CSV string representation of the entity. This method must be implemented
     * by subclasses to provide CSV formatting specific to each entity type.
     *
     * @return A CSV-formatted string representing the entity.
     */
    @Override
    public abstract String toCsvString();
}
