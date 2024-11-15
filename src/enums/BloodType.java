package enums;

/**
 * Enum representing the different blood types, including both positive and negative types.
 * Blood types are essential for medical purposes, including transfusions and donations.
 * 
 * @author Celeste Ho
 * @version 1.0 
 */
public enum BloodType {
    /** Blood type O-positive. Universal donor for positive blood types. */
    O_POSITIVE,
    
    /** Blood type A-positive. */
    A_POSITIVE,
    
    /** Blood type B-positive. */
    B_POSITIVE,
    
    /** Blood type AB-positive. Universal recipient for positive blood types. */
    AB_POSITIVE,
    
    /** Blood type O-negative. Universal donor for all blood types. */
    O_NEGATIVE,
    
    /** Blood type A-negative. */
    A_NEGATIVE,
    
    /** Blood type B-negative. */
    B_NEGATIVE,
    
    /** Blood type AB-negative. Universal recipient for negative blood types. */
    AB_NEGATIVE;
}
