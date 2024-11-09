package model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;

public class Doctor extends User {
    private int startWorkHours;
    private int endWorkHours;

    public Doctor(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender,
        int startWorkHours,
        int endWorkHours,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id,password,salt,name,age,gender,createdAt,updatedAt);
        this.startWorkHours = startWorkHours;
        this.endWorkHours = endWorkHours;
    }

    public int getStartWorkHours(){
        return this.startWorkHours;
    }

    public int getEndWorkHours(){
        return this.endWorkHours;
    }

    @Override
    public String toCsvString(){
        return String.join(",",
            id,
            password,
            new String(salt, StandardCharsets.UTF_8), // from salt
            name,
            String.valueOf(age),
            gender.toString(),
            String.valueOf(startWorkHours),
            String.valueOf(endWorkHours),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
