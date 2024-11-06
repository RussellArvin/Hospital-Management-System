package model;

public class Doctor extends User {
    private int startWorkHours;
    private int endWorkHours;

    public Doctor(
        String id,
        String name,
        String password,
        int startWorkHours,
        int endWorkHours
    ) {
        super(id,password,name);
        this.startWorkHours = startWorkHours;
        this.endWorkHours = endWorkHours;
    }

    @Override
    public String toCsvString(){
        return String.join(",",
            id,
            password,
            name,
            String.valueOf(startWorkHours),
            String.valueOf(endWorkHours),
            createdAt.toString(),
            updatedAt.toString()
        );
    }

    @Override
    public void displayMenu(){
        return;
    }
}
