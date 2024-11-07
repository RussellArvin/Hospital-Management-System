package model;

import java.time.LocalDateTime;

import enums.Gender;

public class Administrator extends User {
    public Administrator(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        super(id, password, salt, name, age, gender, createdAt, updatedAt);
    }
}
