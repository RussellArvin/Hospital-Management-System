package model;

import java.time.LocalDateTime;

import enums.Gender;

public class Doctor extends User {
    private int startWorkHours;
    private int endWorkHours;

    public Doctor(
        String id,
        String name,
        int age,
        Gender gender,
        String password,
        int startWorkHours,
        int endWorkHours,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id,password,name,age,gender,createdAt,updatedAt);
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
