package util;

/**
 * The Constant class provides a collection of commonly used constants
 * in the application, such as default values for passwords and work hours.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class Constant {

    /** 
     * The default password used for initializing user accounts. 
     */
    public static final String DEFAULT_PASSWORD = "password";

    /** 
     * The default starting hour of work for staff members (in 24-hour format). 
     */
    public static final int DEFAULT_START_WORK_HOURS = 9;

    /** 
     * The default ending hour of work for staff members (in 24-hour format). 
     */
    public static final int DEFAULT_END_WORK_HOURS = 17;

    /**
     *  The default CSV headers for staff CSV files
     */
    public static final String DEFAULT_STAFF_CSV_HEADER = "id,password,salt,name,age,gendercreatedAt,updatedAt";
}
