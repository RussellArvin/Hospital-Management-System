package enums;

/**
 * Enum representing the status of a replenishment request.
 * Used to indicate the current state of a request for inventory replenishment.
 * 
 * @author Lim Jun Howe 
 * @version 1.0 
 */
public enum ReplenishmentRequestStatus {
    /** The replenishment request is pending and awaiting approval. */
    PENDING,
    
    /** The replenishment request has been reviewed and rejected. */
    REJECTED,
    
    /** The replenishment request has been reviewed and approved. */
    APPROVED
}
